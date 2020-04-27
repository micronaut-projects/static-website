import React, { Component, Fragment } from "react";
import { Button, ProgressBar, Select } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Icon from "react-materialize/lib/Icon";
import Modal from "react-materialize/lib/Modal";
import Row from "react-materialize/lib/Row";
import TextInput from "react-materialize/lib/TextInput";
import FeatureSelector from "./components/FeatureSelector";

import {
  API_URL,
  JAVA_VERSIONS,
  DEFAULT_JAVA_VERSION,
  DEFAULT_LANG,
  DEFAULT_BUILD,
  DEFAULT_TEST_FW,
} from "./constants";

import TreeView from "@material-ui/lab/TreeView";
import TreeItem from "@material-ui/lab/TreeItem";
import { Grid } from "@material-ui/core";

import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { darcula } from "react-syntax-highlighter/dist/esm/styles/prism";
import { prism } from "react-syntax-highlighter/dist/esm/styles/prism";

import logoLight from "./micronaut.png";
import logoDark from "./micronaut-white.png";
import githubLight from "./github.png";
import githubDark from "./github-white.png";
import twitterLight from "./twitter.png";
import twitterDark from "./twitter-white.png";

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
      info: false,
      error: false,
      styleMode: window.localStorage.getItem("styleMode") || "light",
    };
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
          throw new Error("Error when checking micronaut versions on GitHub");
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
        this.setState({ error: true });
      });
  };

  loadFeatures = (appType) => {
    console.log("Loading features of ", appType);
    this.setState({ loadingFeatures: true });
    fetch(API_URL + "/application-types/" + appType + "/features")
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Error when checking micronaut versions on GitHub");
        }
      })
      .then((data) => {
        this.setState({
          featuresToSelect: data.features,
          loadingFeatures: false,
        });
      })
      .catch((error) => {
        this.setState({ error: true, loadingFeatures: false });
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
    this.setState({ downloading: true });

    const features = this.buildFeaturesQuery();

    let FETCH_URL =
      API_URL +
      "/create/" +
      this.state.type +
      "/" +
      this.state.package +
      "." +
      this.state.name +
      "/?" +
      features +
      "&lang=" +
      this.state.lang +
      "&build=" +
      this.state.build +
      "&test=" +
      this.state.testFw +
      "&javaVersion=" +
      "JDK_" +
      this.state.javaVersion;

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

  loadPreview = () => {
    const features = this.buildFeaturesQuery();

    let FETCH_URL =
      API_URL +
      "/preview/" +
      this.state.type +
      "/" +
      this.state.package +
      "." +
      this.state.name +
      "/?" +
      features +
      "&lang=" +
      this.state.lang +
      "&build=" +
      this.state.build +
      "&test=" +
      this.state.testFw +
      "&javaVersion=" +
      "JDK_" +
      this.state.javaVersion;

    fetch(FETCH_URL, {
      method: "GET",
    })
      .then((response) => response.json())
      .then((json) => {
        let nodes = {};
        let obj = json.contents;
        let node = nodes;
        let keys = Object.keys(obj);
        for (let k = 0; k < keys.length; k++) {
          let key = keys[k];
          let folders = key.split("/");
          let rootNode = node;
          for (let i = 0; i < folders.length; i++) {
            if (i === folders.length - 1) {
              node[folders[i]] = obj[key];
            } else {
              node[folders[i]] = node[folders[i]] || {};
              node = node[folders[i]];
            }
          }
          node = rootNode;
        }
        this.setState({ preview: nodes, downloading: false });
      });
  };

  clearPreview = () => {
    this.setState({
      preview: {},
      currentFile: null,
      currentFileLanguage: null,
    });
  };

  capitalize = (s) => {
    if (typeof s !== "string") return "";
    return s.charAt(0).toUpperCase() + s.slice(1);
  };

  handleFileSelection = (key, contents) => {
    if (typeof contents === "string") {
      let idx = key.lastIndexOf(".");
      let language;
      if (idx > -1) {
        language = key.substring(idx + 1);
        if (language === "gradle") {
          language = "groovy";
        }
        if (language === "bat") {
          language = "batch";
        }
        if (language === "kt") {
          language = "kotlin";
        }
      } else {
        language = "bash";
      }
      this.setState({ currentFile: contents, currentFileLanguage: language });
    }
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
    document.body.className = this.getStyleMode();

    const renderTree = (nodes) => {
      if (nodes instanceof Object) {
        return Object.keys(nodes)
          .sort(function order(key1, key2) {
            let key1Object = typeof nodes[key1] === "object";
            let key2Object = typeof nodes[key2] === "object";
            if (key1Object && !key2Object) {
              return -1;
            } else if (!key1Object && key2Object) {
              return 1;
            } else {
              if (key1 < key2) return -1;
              else if (key1 > key2) return +1;
              else return 0;
            }
          })
          .map((key) => {
            let children = nodes[key];
            return (
              <TreeItem
                nodeId={key}
                label={key}
                onClick={() => this.handleFileSelection(key, children)}
              >
                {renderTree(children)}
              </TreeItem>
            );
          });
      }
    };

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
                  <Col s={6} />
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
                  <Col s={3}>
                    <Modal
                      header={
                        "Previewing a " +
                        this.capitalize(this.state.lang) +
                        " application using " +
                        this.capitalize(this.state.build)
                      }
                      className={"preview " + this.getStyleMode()}
                      fixedFooter
                      options={{
                        onOpenStart: this.loadPreview,
                        onCloseStart: this.clearPreview,
                        startingTop: "5%",
                        endingTop: "5%",
                      }}
                      trigger={
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
                          <Icon left>search</Icon>
                          Preview
                        </Button>
                      }
                    >
                      <Grid container className="grid-container">
                        <Grid
                          item
                          xs={3}
                          className={"grid-column"}
                          style={{ "border-right": "1px solid" }}
                        >
                          <TreeView
                            defaultCollapseIcon={<Icon>folder_open</Icon>}
                            defaultExpandIcon={<Icon>folder</Icon>}
                            defaultEndIcon={<Icon>description</Icon>}
                            defaultExpanded={["src", "main"]}
                          >
                            {renderTree(this.state.preview)}
                          </TreeView>
                        </Grid>
                        <Grid item xs={9} className={"grid-column"}>
                          {this.state.currentFile ? (
                            <SyntaxHighlighter
                              className="codePreview"
                              lineNumberContainerProps={{
                                className: "lineNumbers",
                              }}
                              language={this.state.currentFileLanguage}
                              style={
                                this.getStyleMode() === "light"
                                  ? prism
                                  : darcula
                              }
                              showLineNumbers={true}
                            >
                              {this.state.currentFile}
                            </SyntaxHighlighter>
                          ) : (
                            ""
                          )}
                        </Grid>
                      </Grid>
                    </Modal>
                  </Col>
                </Row>
              </form>
              {this.state.downloading ? <ProgressBar /> : null}
              {this.state.error ? (
                <h5 style={{ color: "red" }}>Oops. An error has ocurred.</h5>
              ) : null}
            </div>
          </div>
        </div>
        <div className="container mn-feature-container">
          <Row>
            <FeatureSelector
              features={this.state.featuresToSelect}
              selectedFeatures={this.state.featuresSelected}
              loading={this.state.loadingFeatures}
              onAddFeature={this.addFeature}
              onRemoveFeature={this.removeFeature}
            />
          </Row>
          <div className="mn-footer">
            <Modal
              open={this.state.info}
              header="What's this?"
              className={this.getStyleMode()}
              trigger={
                <Button
                  floating
                  className={this.getStyleMode()}
                  onClick={() => this.setState({ info: true })}
                >
                  <Icon>info</Icon>
                </Button>
              }
            >
              <p>
                Micronaut Starter is a web application that allows you to create
                Micronaut projects through an interface instead of using the
                console CLI. You can set the application type, the project name,
                the language (Java, Kotlin, Groovy), the build tool (Maven,
                Gradle), the Java version and the features you need to develop
                your software.
              </p>
            </Modal>
            <Button
              floating
              className={this.getStyleMode()}
              onClick={() => this.toggleStyleMode()}
            >
              <Icon>brightness_medium</Icon>
            </Button>
            <a
              href="https://twitter.com/micronautfw"
              target="_blank"
              rel="noopener noreferrer"
              className="mn-footer-logos"
            >
              <img
                src={
                  this.getStyleMode() === "light" ? twitterLight : twitterDark
                }
                alt="Twitter"
                rel="noopener noreferrer"
                height="30px"
                weight="30px"
              />
            </a>
            <a
              href="https://github.com/micronaut-projects"
              target="_blank"
              rel="noopener noreferrer"
              className="mn-footer-logos"
            >
              <img
                src={this.getStyleMode() === "light" ? githubLight : githubDark}
                alt="GitHub"
                rel="noopener noreferrer"
                height="30px"
                weight="30px"
              />
            </a>
          </div>
        </div>
      </Fragment>
    );
  }
}
export default App;
