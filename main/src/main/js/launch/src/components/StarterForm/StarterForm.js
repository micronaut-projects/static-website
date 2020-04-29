// StarterForm.js
import React from "react";
import { Select, TextInput } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Row from "react-materialize/lib/Row";
import RadioGroup from "../RadioGroup";

import {
    JAVA_VERSIONS,
    LANG_OPTS,
    BUILD_OPTS,
    TEST_OPTS,
} from "../../constants";

const StarterForm = ({ handleChange, ...props }) => {
    return (
        <Row className="mn-starter-form-main">
            <Col l={5} m={6} s={12}>
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

            <Col s={6} m={3} l={4}>
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
            <Col s={6} m={3} l={3}>
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
            <Col m={3} s={12} className="mn-radio">
                <RadioGroup
                    label="Language"
                    id="lang"
                    name="lang"
                    value={props.lang}
                    onChange={handleChange}
                    options={LANG_OPTS}
                />
            </Col>
            <Col m={3} s={12} className="mn-radio">
                <RadioGroup
                    label="Build"
                    id="build"
                    name="build"
                    value={props.build}
                    onChange={handleChange}
                    options={BUILD_OPTS}
                />
            </Col>
            <Col m={3} s={12} className="mn-radio">
                <RadioGroup
                    label="Test Framework"
                    id="testFw"
                    name="testFw"
                    value={props.testFw}
                    onChange={handleChange}
                    options={TEST_OPTS}
                />
            </Col>
            <Col m={3} s={12} className="mn-radio">
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
