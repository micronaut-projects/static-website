// StarterForm.js
import React from "react";
import { Select } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Row from "react-materialize/lib/Row";
import TextInput from "react-materialize/lib/TextInput";

import { JAVA_VERSIONS } from "../constants";

const StarterForm = ({ handleChange, ...props }) => {
    return (
        <Row className="mn-starter-form-main">
            <Col m={4} s={12}>
                <Select
                    s={12}
                    className="mn-input"
                    label="Application Type"
                    value={props.type}
                    name="type"
                    onChange={handleChange}
                >
                    {props.types.map((type, i) => {
                        return (
                            <option key={i} value={type.name}>
                                {type.title}
                            </option>
                        );
                    })}
                </Select>
            </Col>

            <Col m={4} s={6}>
                <TextInput
                    required
                    s={12}
                    className="mn-input"
                    label="Name"
                    name="name"
                    placeholder="ex: myapp"
                    value={props.name}
                    onChange={handleChange}
                />
            </Col>
            <Col m={4} s={6}>
                <TextInput
                    required
                    s={12}
                    className="mn-input"
                    label="Base Package"
                    name="package"
                    placeholder="ex: com.mycompany"
                    value={props.package}
                    onChange={handleChange}
                />
            </Col>
            <Col m={3} s={6}>
                <Select
                    s={12}
                    label="Language"
                    value={props.lang}
                    name="lang"
                    onChange={handleChange}
                >
                    <option value="java">Java</option>
                    <option value="kotlin">Kotlin</option>
                    <option value="groovy">Groovy</option>
                </Select>
            </Col>
            <Col m={3} s={6}>
                <Select
                    s={12}
                    label="Build"
                    value={props.build}
                    name="build"
                    onChange={handleChange}
                >
                    <option value="gradle">Gradle</option>
                    <option value="maven">Maven</option>
                </Select>
            </Col>
            <Col m={3} s={6}>
                <Select
                    s={12}
                    label="Test Framework"
                    value={props.testFw}
                    name="testFw"
                    onChange={handleChange}
                >
                    <option value="junit">JUnit</option>
                    <option value="spock">Spock</option>
                    <option value="kotlintest">Kotlintest</option>
                </Select>
            </Col>
            <Col m={3} s={6}>
                <Select
                    s={12}
                    label="Java Version"
                    value={props.javaVersion.toString()}
                    name="javaVersion"
                    onChange={handleChange}
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
    );
};

export default StarterForm;
