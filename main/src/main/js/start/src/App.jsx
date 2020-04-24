import React, { Component, Fragment } from "react";
import { Button, Card, ProgressBar, Select } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Icon from "react-materialize/lib/Icon";
import Modal from "react-materialize/lib/Modal";
import Preloader from "react-materialize/lib/Preloader";
import Row from "react-materialize/lib/Row";
import TextInput from "react-materialize/lib/TextInput";
import TreeView from '@material-ui/lab/TreeView';
import TreeItem from '@material-ui/lab/TreeItem';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
//import { darcula } from 'react-syntax-highlighter/dist/esm/styles/prism'
import { prism } from 'react-syntax-highlighter/dist/esm/styles/prism'
import { API_URL, JAVA_VERSIONS, DEFAULT_JAVA_VERSION, DEFAULT_LANG, DEFAULT_BUILD, DEFAULT_TEST_FW } from './constants';
import logo from "./micronaut.png";
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
      features: [],
      featureSearch: "",
      featuresToSelect: [],
      featureSearchResults: Object.entries({}),
      featuresSelected: Object.entries({}),
      downloading: false,
      info: false,
      error: false
    };
  }

  searchFeature = event => {
    const value = event.target.value;
    this.setState({ featureSearch: value });
    if (value) {
      const featuresMap = new Map(this.state.featuresToSelect);
      const keysSearched = Array.from(featuresMap.keys()).filter(
        el => el.toLowerCase().indexOf(value.toLowerCase()) > -1
      );
      const resultMap = new Map();
      keysSearched.forEach(key => {
        resultMap.set(key, featuresMap.get(key));
      });
      const results = Object.entries(this.mapToObject(resultMap));
      this.setState({ featureSearchResults: results });
    } else {
      this.setState({ featureSearchResults: Object.entries({}) });
    }
  };

  mapToObject = map => {
    const obj = {};
    map.forEach((v, k) => {
      obj[k] = v;
    });
    return obj;
  };

  componentDidMount() {
    this.loadAppTypes()
    this.loadFeatures(this.state.type)
  }

  loadAppTypes = () => {
    fetch(API_URL + '/application-types')
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Error when checking micronaut versions on GitHub');
        }
      })
      .then((data) => {
        console.log("data", data)
        const types = []
        data.types.forEach((t) => {
          types.push({name: t.name.toUpperCase(), title: t.title})
        })
        this.setState({ types })
      })
      .catch((error) => {
        this.setState({error: true})
      })
  }

  loadFeatures = (appType) => {
    console.log("Loading features of ", appType)
    this.setState({loadingFeatures: true})
    fetch(API_URL + '/application-types/' + appType + "/features")
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Error when checking micronaut versions on GitHub');
        }
      })
      .then((data) => {
        console.log("data", data)
        const features = []
        data.features.forEach((t) => {
          features.push([t.name, t.description])
        })
        this.setState({ featuresToSelect: features, loadingFeatures: false })
      })
      .catch((error) => {
        this.setState({error: true, loadingFeatures: false})
      })
  }

  addFeature = feature => {
    let featuresSelected = this.state.featuresSelected;
    let featuresToSelect = this.state.featuresToSelect;
    let features = this.state.features;
    featuresSelected.push(feature);
    features.push(feature[0]);
    let indexOfFeature;
    // eslint-disable-next-line
    featuresToSelect.map((f, i) => {
      if (f[0] === feature[0]) {
        return (indexOfFeature = i);
      }
    });
    featuresToSelect.splice(indexOfFeature, 1);
    this.setState({
      featuresToSelect,
      featuresSelected,
      features,
      featureSearchResults: Object.entries({}),
      featureSearch: ""
    });
  };

  removeFeature = (e, feature, index) => {
    e.preventDefault();
    const featuresSelected = this.state.featuresSelected;
    const featuresToSelect = this.state.featuresToSelect;
    featuresSelected.splice(index, 1);
    featuresToSelect.push(feature);
    this.setState({ featuresSelected, featuresToSelect });
  };

  generateProject = e => {
    e.preventDefault();
    this.setState({ downloading: true });

    let features = ""

    this.state.featuresSelected.forEach((feature) => {
      features += "features=" + feature[0] + "&"
    })

    let FETCH_URL =
      API_URL + "/create/" + this.state.type + "/" + 
      this.state.package + "." + this.state.name + "/?" +
      features +
      "lang=" +
      this.state.lang +
      "&build=" +
      this.state.build +
      "&test=" +
      this.state.testFw +
      "&javaVersion=" +
      "JDK_" + this.state.javaVersion;

    fetch(FETCH_URL, {
      method: "GET"
    })
      .then(response => response.blob())
      .then(blob => {
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

  handleChange = event => {
    this.setState({ [event.target.name]: event.target.value });
    if (event.target.name === "type") {
      this.loadFeatures(event.target.value)
    }
  };

  loadPreview = () => {
    let features = ""

    this.state.featuresSelected.forEach((feature) => {
      features += "features=" + feature[0] + "&"
    })

    let FETCH_URL =
        API_URL + "/preview/" + this.state.type + "/" +
        this.state.package + "." + this.state.name + "/?" +
        features +
        "lang=" +
        this.state.lang +
        "&build=" +
        this.state.build +
        "&test=" +
        this.state.testFw +
        "&javaVersion=" +
        "JDK_" + this.state.javaVersion;

    fetch(FETCH_URL, {
      method: "GET"
    })
        .then(response => response.json())
        .then(json => {
          let nodes = {};
          let obj = json.contents;
          let node = nodes;
          let keys = Object.keys(obj);
          for (let k = 0; k < keys.length; k++) {
            let key = keys[k];
            let folders = key.split("/");
            let rootNode = node;
            for (let i = 0; i < folders.length; i++) {
              if (i === (folders.length - 1)) {
                node[folders[i]] = obj[key];
              } else {
                node[folders[i]] = node[folders[i]] || {};
                node = node[folders[i]];
              }
            }
            node = rootNode;
          }
          this.setState({preview: nodes});
        });

  };

  clearPreview = () => {
    this.setState({preview: {}, currentFile: null, currentFileLanguage: null});
  }


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
        language = "bash"
      }
      this.setState({currentFile: contents, currentFileLanguage: language});
    }
  }


  render() {
    const renderTree = (nodes) => {
      if (nodes instanceof Object) {
        return Object.keys(nodes).map(key => {
          let children = nodes[key];
          return (
              <TreeItem nodeId={key} label={key} onClick={() => this.handleFileSelection(key, children)}>
                {renderTree(children)}
              </TreeItem>
          );
        });
      }
    };

    return (
      <Fragment>
        <div className="container">
          <img src={logo} width="50%" alt="Micronaut" className="mn-logo" />
          <div className="mn-container">
            <form onSubmit={this.generateProject} autoComplete="off">
              <Row>
                <Col s={4}>
                    <Select
                      s={12}
                      label="Application Type"
                      value={this.state.type}
                      name="type"
                      onChange={this.handleChange}
                    >
                      {
                        this.state.types.map((type, i) => {
                          return <option key={i} value={type.name}>{type.title}</option>
                        })
                      }
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
                    value={this.state.javaVersion}
                    name="javaVersion"
                    onChange={this.handleChange}
                  >
                    {
                      JAVA_VERSIONS.map((version, i) => {
                          return <option key={i} value={version}>{version}</option>
                      })
                    }
                  </Select>
                </Col>
              </Row>
              <Row>
                {this.state.loadingFeatures ?
                <Col s={6}>
                  <Preloader
                    active
                    flashing={false}
                    size="big"
                  />
                </Col>
                :
                <Col s={6}>
                  <TextInput
                    className="mn-input"
                    s={12}
                    label="Features"
                    placeholder="ex: cassandra"
                    value={this.state.featureSearch}
                    onChange={this.searchFeature}
                  />
                  {this.state.featureSearch &&
                    this.state.featureSearchResults.length === 0 ? (
                      <Col s={12}>
                        <p className="grey-text">No results.</p>
                      </Col>
                    ) : null}
                  {this.state.featureSearchResults.map((feature, i) => (
                    <Col s={12} key={i}>
                      <Card
                        className="white mn-feature-selection"
                        title={feature[0]}
                        onClick={() => this.addFeature(feature)}
                      >
                        <p className="grey-text">{feature[1]}</p>
                      </Card>
                    </Col>
                  ))}
                </Col>}
                <Col s={6}>
                  <b>Selected features</b>
                  {this.state.featuresSelected.length === 0 ? (
                    <p className="grey-text">No features selected.</p>
                  ) : null}
                  <Row>
                    {this.state.featuresSelected.map((feature, i) => (
                      <Card className="white" title={feature[0]} key={i}>
                        <Row>
                          <Col s={11}>
                            <p className="grey-text">{feature[1]}</p>
                          </Col>
                          <Col s={1}>
                            <Button
                              floating
                              small
                              className="black"
                              onClick={(e) => this.removeFeature(e, feature, i)}
                            >
                              <Icon>close</Icon>
                            </Button>
                          </Col>
                        </Row>
                      </Card>
                    ))}
                  </Row>
                </Col>
              </Row>
              <Row>
                <Col s={3}>
                  <Button
                    disabled={this.state.downloading || !this.state.name || !this.state.package || this.state.loadingFeatures}
                    waves="light"
                    style={{ marginRight: "5px", backgroundColor: "black" }}
                  >
                    <Icon left>add</Icon>
                    Generate project
                  </Button>
                </Col>
                <Col s={3}>
                  <Modal
                      header="Preview"
                      className="wide"
                      options={{
                        onOpenStart: this.loadPreview,
                        onCloseStart: this.clearPreview
                      }}
                      trigger={
                        <Button
                            disabled={this.state.downloading || !this.state.name || !this.state.package || this.state.loadingFeatures}
                            waves="light"
                            style={{ marginRight: "5px", backgroundColor: "black" }}
                        >
                          <Icon left>search</Icon>
                          Preview
                        </Button>
                      }
                  >

                    <Row>
                      <Col s={3}>
                        <TreeView
                            defaultCollapseIcon={<Icon>folder_open</Icon>}
                            defaultExpandIcon={<Icon>folder</Icon>}
                            defaultEndIcon={<Icon>insert_drive_file</Icon>}
                            defaultExpanded={['src', 'main']}
                        >
                          {renderTree(this.state.preview)}
                        </TreeView>
                      </Col>
                      <Col s={9} >

                        {this.state.currentFile ?
                          <SyntaxHighlighter language={this.state.currentFileLanguage} style={prism} showLineNumbers={true} >
                          {this.state.currentFile}
                          </SyntaxHighlighter> : ""
                        }
                      </Col>
                    </Row>
                  </Modal>
                </Col>
              </Row>
            </form>
            {this.state.downloading ? <ProgressBar /> : null}
            {this.state.error ? <h5 style={{color: "red"}}>Oops. An error has ocurred.</h5> : null}
          </div>
        </div>
        <div className="mn-footer">
          <Modal
            open={this.state.info}
            header="What's this?"
            trigger={
              <Button
                floating
                className="black"
                onClick={() => this.setState({ info: true })}
              >
                <Icon>info</Icon>
              </Button>
            }
          >
            <p>
              Micronaut Starter is a web application that allows you to create Micronaut projects through an interface instead of using the console CLI.
              You can set the application type, the project name, the language (Java, Kotlin, Groovy), the build tool (Maven, Gradle), the Java version
              and the features you need to develop your software.
            </p>
          </Modal>
          <a
            href="https://twitter.com/micronautfw"
            target="_blank"
            rel="noopener noreferrer"
            className="mn-footer-logos"
          >
            <img
              src="https://image.flaticon.com/icons/png/512/23/23931.png"
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
              src="https://image.flaticon.com/icons/svg/25/25231.svg"
              alt="GitHub"
              rel="noopener noreferrer"
              height="30px"
              weight="30px"
            />
          </a>
        </div>
      </Fragment>
    );
  }
}
export default App;