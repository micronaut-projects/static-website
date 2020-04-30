// StarterForm.js
import React from "react";
import { TextInput } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Row from "react-materialize/lib/Row";
import RadioGroup from "../RadioGroup";
import Select from "../Select";

import {
    JAVA_VERSIONS,
    LANG_OPTS,
    BUILD_OPTS,
    TEST_OPTS,
} from "../../constants";

const JAVA_OPTS = JAVA_VERSIONS.map((v) => ({
    label: v.toString(),
    value: v.toString(),
}));

const StarterForm = ({ handleChange, ...props }) => {
    return (
        <Row className="mn-starter-form-main">
            <Col l={4} m={8} s={8}>
                <Select
                    s={12}
                    className="mn-input"
                    label="Application Type"
                    value={props.type}
                    name="type"
                    options={props.types.map((t) => ({
                        value: t.name,
                        label: t.title,
                    }))}
                    onChange={handleChange}
                ></Select>
            </Col>
            <Col l={2} m={4} s={4} className="mn-radio">
                <Select
                    s={12}
                    label="Java Version"
                    value={props.javaVersion.toString()}
                    name="javaVersion"
                    onChange={handleChange}
                    options={JAVA_OPTS}
                ></Select>
            </Col>
            <Col s={8} m={6} l={3}>
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
            <Col s={4} m={6} l={3}>
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
        </Row>
    );
};

export default StarterForm;
