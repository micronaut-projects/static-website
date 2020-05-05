import React, { Fragment, useState, useEffect, useRef } from "react";
import { ProgressBar } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Icon from "react-materialize/lib/Icon";
import Row from "react-materialize/lib/Row";
import {
  FeatureSelectorModal,
  FeatureSelectedList,
} from "./components/FeatureSelector";
import CodePreview from "./components/CodePreview";
import Diff from "./components/Diff";
import Header from "./components/Header";
import StarterForm from "./components/StarterForm";
import ErrorView from "./components/ErrorView";
import TooltipButton from "./components/TooltipButton";
import OciHomeOfMicronaut from "./components/OciHomeOfMicronaut";

import {
  API_URL,
  DEFAULT_JAVA_VERSION,
  DEFAULT_LANG,
  DEFAULT_BUILD,
  DEFAULT_TEST_FW,
} from "./constants";
import messages from "./constants/messages.json";

import useAppTheme from "./hooks/useAppTheme";

import {
  downloadBlob,
  makeNodeTree,
  responseHandler,
  debounceResponse,
} from "./utility";

import "./style.css";
import "./styles/button-row.css";
import "./styles/modal-overrides.css";
import "./styles/utility.css";

export default function App() {
  const [form, setForm] = useState({
    name: "demo",
    package: "com.example",
    type: "DEFAULT",
    lang: DEFAULT_LANG,
    build: DEFAULT_BUILD,
    testFw: DEFAULT_TEST_FW,
    javaVersion: DEFAULT_JAVA_VERSION,
  });

  const [types, setTypes] = useState([{ name: "DEFAULT", title: "" }]);
  const [featuresAvailable, setFeaturesAvailable] = useState([]);
  const [featuresSelected, setFeaturesSelected] = useState({});

  const [loadingFeatures, setLoadingFeatures] = useState(false);
  const [downloading, setDownloading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [preview, setPreview] = useState({});
  const [diff, setDiff] = useState(null);

  const [theme, toggleTheme] = useAppTheme();
  const previewButton = useRef();
  const diffButton = useRef();

  const disabled =
    downloading || loadingFeatures || !form.name || !form.package;
  const hasError = Boolean(errorMessage);
  const appType = form.type;

  useEffect(() => {
    const load = async () => {
      setDownloading(true);
      try {
        const data = await fetch(API_URL + "/application-types").then(
          responseHandler("json")
        );
        const types = data.types.map((t) => {
          return { name: t.name.toUpperCase(), title: t.title };
        });
        setTypes(types);
      } catch (error) {
        await handleResponseError(error);
      } finally {
        setDownloading(false);
      }
    };
    load();
  }, []);

  useEffect(() => {
    const loadFeatures = async () => {
      setLoadingFeatures(true);
      setErrorMessage("");
      try {
        const data = await fetch(
          API_URL + "/application-types/" + appType + "/features"
        ).then(responseHandler("json"));

        setFeaturesAvailable(data.features);
      } catch (error) {
        setErrorMessage(error.messages);
      } finally {
        setLoadingFeatures(false);
      }
    };
    loadFeatures();
  }, [appType]);

  const buildFeaturesQuery = () => {
    return Object.keys(featuresSelected)
      .reduce((array, feature) => {
        array.push(`features=${feature}`);
        return array;
      }, [])
      .join("&");
  };

  const buildFetchUrl = (prefix, form) => {
    if (!prefix) {
      console.error(
        "A prefix is required, should be one of 'diff', 'preview' or 'create'"
      );
    }
    const { type, name, lang, build, testFw, javaVersion, package: pkg } = form;
    const features = buildFeaturesQuery();
    const fqpkg = `${pkg}.${name}`;
    const base = `${API_URL}/${prefix}/${type}/${fqpkg}`;
    const query = [
      `lang=${lang}`,
      `build=${build}`,
      `test=${testFw}`,
      `javaVersion=JDK_${javaVersion}`,
    ];
    if (features) {
      query.push(features);
    }
    return encodeURI(`${base}?${query.join("&")}`);
  };

  const handleResponseError = async (response) => {
    if (response instanceof Error) {
      return setErrorMessage(response.message);
    }

    let defaultError = "something went wrong.";
    if (!response.json instanceof Function) {
      setErrorMessage(defaultError);
      return;
    }
    try {
      const json = await response.json();
      setErrorMessage(json.message || defaultError);
    } catch (e) {
      setErrorMessage(defaultError);
    }
  };

  const addFeature = (feature) => {
    setFeaturesSelected(({ ...draft }) => {
      draft[feature.name] = feature;
      return draft;
    });
  };

  const removeFeature = (feature) => {
    setFeaturesSelected(({ ...draft }) => {
      delete draft[feature.name];
      return draft;
    });
  };

  const removeAllFeatures = () => {
    setFeaturesSelected({});
  };

  const handleChange = (event) => {
    // Strip out any non alphanumeric characters (or ".","-","_") from the input.
    const { name: key, value } = event.target;
    setForm((draft) => ({
      ...draft,
      [key]: value.replace(/[^a-z\d.\-_]/gi, ""),
    }));
  };

  const requestPrep = (event) => {
    if (event && event.preventDefault instanceof Function) {
      event.preventDefault();
    }
    setErrorMessage("");
    setDownloading(true);
  };

  const generateProject = async (e) => {
    requestPrep(e);
    const url = buildFetchUrl("create", form);
    // Debounce the download event so the UI is not jumpy
    const debounced = debounceResponse(Date.now());
    try {
      const blob = await fetch(url)
        .then(debounced)
        .then(responseHandler("blob"));

      downloadBlob(blob, `${form.name}.zip`);
    } catch (error) {
      await handleResponseError(error);
    } finally {
      setDownloading(false);
    }
  };

  const loadPreview = async (e) => {
    requestPrep(e);
    try {
      let url = buildFetchUrl("preview", form);
      // Debounce the preview gen event so the UI is not jumpy
      const debounced = debounceResponse(Date.now());
      const json = await fetch(url)
        .then(debounced)
        .then(responseHandler("json"));

      const nodes = makeNodeTree(json.contents);
      setPreview(nodes);
      previewButton.current.props.onClick();
    } catch (error) {
      await handleResponseError(error);
    } finally {
      setDownloading(false);
    }
  };

  const loadDiff = async (e) => {
    requestPrep(e);
    try {
      let url = buildFetchUrl("diff", form);
      // Debounce the preview gen event so the UI is not jumpy
      const debounced = debounceResponse(Date.now());
      const text = await fetch(url)
        .then(debounced)
        .then(responseHandler("text"));
      if (text === "") {
        throw new Error(
          "No features have been selected. Please choose one or more features and try again."
        );
      }
      setDiff(text);
      diffButton.current.props.onClick();
    } catch (error) {
      await handleResponseError(error);
    } finally {
      setDownloading(false);
    }
  };

  const clearDiff = () => {
    setDiff(null);
  };

  const clearPreview = () => {
    setPreview({});
  };

  return (
    <Fragment>
      <div id="mn-main-container" className="mn-main-container sticky">
        <div className="container">
          <Header theme={theme} onToggleTheme={toggleTheme} />

          <div className="mn-container">
            <form onSubmit={generateProject} autoComplete="off">
              <StarterForm
                theme={theme}
                handleChange={handleChange}
                types={types}
                {...form}
              />

              <Row className="button-row">
                <Col s={3} className="xs6">
                  <FeatureSelectorModal
                    theme={theme}
                    loading={loadingFeatures}
                    features={featuresAvailable}
                    selectedFeatures={featuresSelected}
                    onAddFeature={addFeature}
                    onRemoveFeature={removeFeature}
                    onRemoveAllFeatures={removeAllFeatures}
                  />
                </Col>
                <Col s={3} className="xs6">
                  <Diff
                    ref={diffButton}
                    theme={theme}
                    diff={diff}
                    lang={form.lang}
                    build={form.build}
                    onLoad={loadDiff}
                    onClose={clearDiff}
                    disabled={disabled}
                  />
                </Col>
                <Col s={3} className="xs6">
                  <CodePreview
                    ref={previewButton}
                    theme={theme}
                    preview={preview}
                    lang={form.lang}
                    build={form.build}
                    onLoad={loadPreview}
                    onClose={clearPreview}
                    disabled={disabled}
                  />
                </Col>
                <Col s={3} className="xs6">
                  <TooltipButton
                    tooltip={messages.tooltips.generate}
                    disabled={disabled}
                    waves="light"
                    className={theme}
                    style={{ marginRight: "5px", width: "100%" }}
                  >
                    <Icon className="action-button-icon" left>
                      get_app
                    </Icon>
                    Generate project
                  </TooltipButton>
                </Col>
              </Row>
            </form>
            <div className="progress-container">
              {downloading && <ProgressBar />}
            </div>
          </div>
        </div>
      </div>
      <div className="container mn-feature-container">
        <FeatureSelectedList
          theme={theme}
          selectedFeatures={featuresSelected}
          onRemoveFeature={removeFeature}
        />
      </div>
      <footer className="container mn-footer-container">
        <OciHomeOfMicronaut />
      </footer>
      <ErrorView
        error={hasError}
        errorMessage={errorMessage}
        onClose={() => setErrorMessage("")}
      />
    </Fragment>
  );
}
