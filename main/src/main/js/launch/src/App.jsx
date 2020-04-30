import React, { Component, Fragment } from "react";
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
import Footer from "./components/Footer";
import Header from "./components/Header";
import StarterForm from "./components/StarterForm";
import ErrorView from "./components/ErrorView";
import TooltipButton from "./components/TooltipButton";

import {
  API_URL,
  DEFAULT_JAVA_VERSION,
  DEFAULT_LANG,
  DEFAULT_BUILD,
  DEFAULT_TEST_FW,
} from "./constants";
import messages from "./constants/messages.json";

import { makeNodeTree } from "./utility";

import logoLight from "./micronaut.png";
import logoDark from "./micronaut-white.png";

import "./style.css";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "demo",
      package: "com.example",
      types: [],
      type: "DEFAULT",
      lang: DEFAULT_LANG,
      build: DEFAULT_BUILD,
      testFw: DEFAULT_TEST_FW,
      javaVersion: DEFAULT_JAVA_VERSION,
      loadingFeatures: false,
      featuresToSelect: [],
      featuresSelected: {},
      downloading: false,
      error: false,
      errorMessage: "",
      styleMode: window.localStorage.getItem("styleMode") || "light",
    };
    this.previewButton = null;
    this.diffButton = null;
  }

  componentDidMount() {
    this.loadAppTypes();
    this.loadFeatures(this.state.type);
  }

  loadAppTypes = () => {
    fetch(API_URL + "/application-types")
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Failed to load the application types");
        }
      })
      .then((data) => {
        console.log("data", data);
        const types = [];
        data.types.forEach((t) => {
          types.push({ name: t.name.toUpperCase(), title: t.title });
        });
        this.setState({ types });
      })
      .catch((error) => {
        this.setState({ error: true, errorMessage: error.message });
      });
  };

  loadFeatures = (appType) => {
    this.setState({ loadingFeatures: true });
    fetch(API_URL + "/application-types/" + appType + "/features")
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Failed to load the available features");
        }
      })
      .then((data) => {
        this.setState({
          featuresToSelect: data.features,
          loadingFeatures: false,
        });
      })
      .catch((error) => {
        this.setState({
          error: true,
          loadingFeatures: false,
          errorMessage: error.message,
        });
      });
  };

  addFeature = (feature) => {
    let featuresSelected = { ...this.state.featuresSelected };
    featuresSelected[feature.name] = feature;
    this.setState({
      featuresSelected,
    });
  };

  removeAllFeatures = () => {
    this.setState({ featuresSelected: {} });
  };

  removeFeature = (feature) => {
    let featuresSelected = { ...this.state.featuresSelected };
    delete featuresSelected[feature.name];
    this.setState({ featuresSelected });
  };

  handleChange = (event) => {
    // Strip out any non alphanumeric characters (or ".","-","_") from the input.
    const value = event.target.value.replace(/[^a-z\d.\-_]/gi, "");
    this.setState({
      [event.target.name]: value,
    });
    if (event.target.name === "type") {
      this.loadFeatures(event.target.value);
    }
  };

  buildFeaturesQuery = () => {
    return Object.keys(this.state.featuresSelected)
      .reduce((array, feature) => {
        array.push(`features=${feature}`);
        return array;
      }, [])
      .join("&");
  };

  buildFetchUrl = (prefix) => {
    if (!prefix) {
      console.error(
        "A prefix is required, should be one of 'diff', 'preview' or 'create'"
      );
    }
    const { type, name, lang, build, testFw, javaVersion } = this.state;
    const features = this.buildFeaturesQuery();
    const fqpkg = `${this.state.package}.${name}`;
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

  handleResponseError = (response) => {
    let defaultError = {
      error: true,
      downloading: false,
      errorMessage: "something went wrong.",
    };
    if (!response.json instanceof Function) {
      this.setState(defaultError);
      return;
    }
    try {
      response.json().then((body) => {
        this.setState({
          ...defaultError,
          errorMessage: body.message,
        });
      });
    } catch (e) {
      this.setState(defaultError);
    }
  };

  responseHandler = (type = "json") => (response) => {
    if (!response.ok) {
      throw response;
    }
    return response[type]();
  };

  debounceResponse = (start, atLeast = 700) => (response) => {
    const end = Date.now();
    const diff = end - start;
    return new Promise((r) => {
      setTimeout(() => {
        r(response);
      }, Math.max(atLeast - diff, 0));
    });
  };

  generateProject = (e) => {
    e.preventDefault();
    this.setState({ error: false, downloading: true });

    const FETCH_URL = this.buildFetchUrl("create");
    // Debounce the download event so the UI is not jumpy
    const debounced = this.debounceResponse(Date.now());
    fetch(FETCH_URL, {
      method: "GET",
    })
      .then(debounced)
      .then(this.responseHandler("blob"))
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement("a");
        a.href = url;
        a.download = this.state.name + ".zip";
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove(); //afterwards we remove the element again
        this.setState({ downloading: false });
      })
      .catch(this.handleResponseError);
  };

  loadPreview = (e) => {
    e.preventDefault();
    this.setState({ error: false, downloading: true });
    let FETCH_URL = this.buildFetchUrl("preview");

    // Debounce the preview gen event so the UI is not jumpy
    const debounced = this.debounceResponse(Date.now());
    fetch(FETCH_URL, {
      method: "GET",
    })
      .then(debounced)
      .then(this.responseHandler("json"))
      .then((json) => {
        const nodes = makeNodeTree(json.contents);
        this.setState({ preview: nodes, downloading: false });
        this.previewButton.props.onClick();
      })
      .catch(this.handleResponseError);
  };

  loadDiff = (e) => {
    this.setState({ error: false });
    e.preventDefault();
    let FETCH_URL = this.buildFetchUrl("diff");

    fetch(FETCH_URL, {
      method: "GET",
    })
      .then(this.responseHandler("text"))
      .then((text) => {
        if (text === "") {
          this.setState({
            diff: "There are no differences. Try selecting some features.",
            downloading: false,
          });
        } else {
          this.setState({ diff: text, downloading: false });
        }

        this.diffButton.props.onClick();
      })
      .catch(this.handleResponseError);
  };

  clearDiff = () => {
    this.setState({
      diff: null,
    });
  };

  clearPreview = () => {
    this.setState({
      preview: {},
    });
  };

  getStyleMode() {
    return this.state.styleMode;
  }

  toggleStyleMode() {
    let style = this.getStyleMode() === "light" ? "dark" : "light";
    this.setState({ styleMode: style });
    window.localStorage.setItem("styleMode", style);
  }

  render() {
    const theme = this.getStyleMode();
    document.body.className = theme;
    const disabled =
      this.state.downloading ||
      !this.state.name ||
      !this.state.package ||
      this.state.loadingFeatures;

    return (
      <Fragment>
        <div id="mn-main-container" className="mn-main-container sticky">
          <div className="container">
            <Header
              theme={theme}
              theme={theme}
              onToggleTheme={() => this.toggleStyleMode()}
            />

            <div className="mn-container">
              <form onSubmit={this.generateProject} autoComplete="off">
                <StarterForm
                  theme={theme}
                  handleChange={this.handleChange}
                  {...this.state}
                />

                <Row className="button-row">
                  <Col s={3}>
                    <FeatureSelectorModal
                      theme={theme}
                      loading={this.state.loadingFeatures}
                      features={this.state.featuresToSelect}
                      selectedFeatures={this.state.featuresSelected}
                      onAddFeature={this.addFeature}
                      onRemoveFeature={this.removeFeature}
                      onRemoveAllFeatures={this.removeAllFeatures}
                    />
                  </Col>
                  <Col s={3}>
                    <Diff
                      ref={(button) => (this.diffButton = button)}
                      theme={theme}
                      diff={this.state.diff}
                      lang={this.state.lang}
                      build={this.state.build}
                      onLoad={this.loadDiff}
                      onClose={this.clearDiff}
                      disabled={
                        this.state.downloading ||
                        !this.state.name ||
                        !this.state.package ||
                        this.state.loadingFeatures
                      }
                    />
                  </Col>
                  <Col s={3}>
                    <CodePreview
                      ref={(button) => (this.previewButton = button)}
                      theme={theme}
                      preview={this.state.preview}
                      lang={this.state.lang}
                      build={this.state.build}
                      onLoad={this.loadPreview}
                      onClose={this.clearPreview}
                      disabled={disabled}
                    />
                  </Col>
                  <Col s={3}>
                    <TooltipButton
                      tooltip={messages.tooltips.generate}
                      disabled={disabled}
                      waves="light"
                      className={theme}
                      style={{ marginRight: "5px", width: "100%" }}
                    >
                      <Icon left>get_app</Icon>
                      Generate project
                    </TooltipButton>
                  </Col>
                </Row>
              </form>
              {this.state.downloading ? (
                <ProgressBar />
              ) : (
                <div style={{ minHeight: "18px", height: "18px" }} />
              )}
            </div>
          </div>
        </div>
        <div className="container mn-feature-container">
          <FeatureSelectedList
            theme={theme}
            selectedFeatures={this.state.featuresSelected}
            onRemoveFeature={this.removeFeature}
          />
        </div>
        <ErrorView
          error={this.state.error}
          errorMessage={this.state.errorMessage}
          onClose={() => this.setState({ error: false, errorMessage: "" })}
        />
      </Fragment>
    );
  }
}
export default App;
