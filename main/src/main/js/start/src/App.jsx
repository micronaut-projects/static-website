import React, { Component, Fragment } from "react";
import { Button, ProgressBar, Select } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Icon from "react-materialize/lib/Icon";
import Row from "react-materialize/lib/Row";
import TextInput from "react-materialize/lib/TextInput";
import FeatureSelector from "./components/FeatureSelector";
import CodePreview from "./components/CodePreview";
import Footer from "./components/Footer";

import {
  API_URL,
  JAVA_VERSIONS,
  DEFAULT_JAVA_VERSION,
  DEFAULT_LANG,
  DEFAULT_BUILD,
  DEFAULT_TEST_FW,
} from "./constants";

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
      search: "",
    };
    this.modalButton = null;
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

  removeFeature = (e, feature, index) => {
    e.preventDefault();
    let featuresSelected = { ...this.state.featuresSelected };
    delete featuresSelected[feature.name];
    this.setState({ featuresSelected });
  };

  generateProject = (e) => {
    e.preventDefault();
    this.setState({ error: false, downloading: true });

    const FETCH_URL = this.buildFetchUrl("create");
    fetch(FETCH_URL, {
      method: "GET",
    })
      .then((response) => response.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement("a");
        a.href = url;
        a.download = this.state.name + ".zip";
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove(); //afterwards we remove the element again
        this.setState({ downloading: false });
      });
  };

  handleChange = (event) => {
    this.setState({ [event.target.name]: event.target.value });
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
        "A prefix is required, should be either 'preview' or 'create'"
      );
    }
    const { type, name, lang, build, testFw, javaVersion } = this.state;
    const features = this.buildFeaturesQuery();
    const base = `${API_URL}/${prefix}/${type}/${this.state.package}.${name}`;
    const query = [
      `lang=${lang}`,
      `build=${build}`,
      `test=${testFw}`,
      `javaVersion=JDK_${javaVersion}`,
    ];
    if (features) {
      query.push(features);
    }
    return `${base}?${query.join("&")}`;
  };

  loadPreview = (e) => {
    this.setState({ error: false });
    e.preventDefault();
    let FETCH_URL = this.buildFetchUrl("preview");

    fetch(FETCH_URL, {
      method: "GET",
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw response;
        }
      })
      .then((json) => {
        const nodes = makeNodeTree(json.contents);
        this.setState({ preview: nodes, downloading: false });
        this.modalButton.props.onClick();
      })
      .catch((response) => {
        console.log(response);
        response.json().then((body) => {
          this.setState({ error: true, errorMessage: body.message });
        });
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

    return (
      <Fragment>
        <div className="mn-main-container sticky">
          <div className="container">
            <img
              src={this.getStyleMode() === "light" ? logoLight : logoDark}
              width="50%"
              alt="Micronaut"
              className="mn-logo"
            />
            <div className="mn-container">
              {this.state.error ? (
                <h5 style={{ color: "red" }}>{this.state.errorMessage}</h5>
              ) : null}
              <form onSubmit={this.generateProject} autoComplete="off">
                <Row>
                  <Col s={4}>
                    <Select
                      s={12}
                      className="mn-input"
                      label="Application Type"
                      value={this.state.type}
                      name="type"
                      onChange={this.handleChange}
                    >
                      {this.state.types.map((type, i) => {
                        return (
                          <option key={i} value={type.name}>
                            {type.title}
                          </option>
                        );
                      })}
                    </Select>
                  </Col>

                  <Col s={4}>
                    <TextInput
                      required
                      s={12}
                      className="mn-input"
                      label="Name"
                      name="name"
                      placeholder="ex: myapp"
                      value={this.state.name}
                      onChange={this.handleChange}
                    />
                  </Col>
                  <Col s={4}>
                    <TextInput
                      required
                      s={12}
                      className="mn-input"
                      label="Base Package"
                      name="package"
                      placeholder="ex: com.mycompany"
                      value={this.state.package}
                      onChange={this.handleChange}
                    />
                  </Col>
                </Row>
                <Row>
                  <Col s={3}>
                    <Select
                      s={12}
                      label="Language"
                      value={this.state.lang}
                      name="lang"
                      onChange={this.handleChange}
                    >
                      <option value="java">Java</option>
                      <option value="kotlin">Kotlin</option>
                      <option value="groovy">Groovy</option>
                    </Select>
                  </Col>
                  <Col s={3}>
                    <Select
                      s={12}
                      label="Build"
                      value={this.state.build}
                      name="build"
                      onChange={this.handleChange}
                    >
                      <option value="gradle">Gradle</option>
                      <option value="maven">Maven</option>
                    </Select>
                  </Col>
                  <Col s={3}>
                    <Select
                      s={12}
                      label="Test Framework"
                      value={this.state.testFw}
                      name="testFw"
                      onChange={this.handleChange}
                    >
                      <option value="junit">JUnit</option>
                      <option value="spock">Spock</option>
                      <option value="kotlintest">Kotlintest</option>
                    </Select>
                  </Col>
                  <Col s={3}>
                    <Select
                      s={12}
                      label="Java Version"
                      value={this.state.javaVersion.toString()}
                      name="javaVersion"
                      onChange={this.handleChange}
                    >
                      {JAVA_VERSIONS.map((version, i) => {
                        return (
                          <option key={i} value={version.toString()}>
                            {version}
                          </option>
                        );
                      })}
                    </Select>
                  </Col>
                </Row>

                <Row>
                  <Col s={6}>
                    <TextInput
                      className="mn-input"
                      s={12}
                      label="Features"
                      placeholder="ex: cassandra"
                      name="search"
                      onChange={this.handleChange}
                    />
                  </Col>
                  <Col s={3}>
                    <CodePreview
                      ref={(button) => (this.modalButton = button)}
                      preview={this.state.preview}
                      lang={this.state.lang}
                      build={this.state.build}
                      theme={theme}
                      onLoad={this.loadPreview}
                      onClose={this.clearPreview}
                      disabled={
                        this.state.downloading ||
                        !this.state.name ||
                        !this.state.package ||
                        this.state.loadingFeatures
                      }
                    />
                  </Col>
                  <Col s={3}>
                    <Button
                      disabled={
                        this.state.downloading ||
                        !this.state.name ||
                        !this.state.package ||
                        this.state.loadingFeatures
                      }
                      waves="light"
                      className={this.getStyleMode()}
                      style={{ marginRight: "5px" }}
                    >
                      <Icon left>get_app</Icon>
                      Generate project
                    </Button>
                  </Col>
                </Row>
              </form>
              {this.state.downloading ? <ProgressBar /> : null}
            </div>
          </div>
        </div>
        <div className="container mn-feature-container">
          <Row>
            <FeatureSelector
              features={this.state.featuresToSelect}
              selectedFeatures={this.state.featuresSelected}
              loading={this.state.loadingFeatures}
              search={this.state.search}
              onAddFeature={this.addFeature}
              onRemoveFeature={this.removeFeature}
            />
          </Row>
          <Footer theme={theme} onToggleTheme={() => this.toggleStyleMode()} />
        </div>
      </Fragment>
    );
  }
}
export default App;
