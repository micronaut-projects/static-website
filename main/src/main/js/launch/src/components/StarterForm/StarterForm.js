// StarterForm.js
import React, { useMemo } from "react";
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
    MICRONAUT_VERSIONS,
} from "../../constants";

const JAVA_OPTS = JAVA_VERSIONS.map((v) => ({
    label: v.toString(),
    value: v.toString(),
}));

const FAKE_ChangeHandler = () => {};

const StarterForm = ({ handleChange, ...props }) => {
    const applicationTypeOpts = useMemo(() => {
        return props.types.map((t) => ({
            value: t.name,
            label: t.title.replace("Micronaut ", ""),
        }));
    }, [props.types]);

    return (
        <Row className="mn-starter-form-main">
            <Col s={8} m={6} l={3}>
                <Select
                    className="mn-input"
                    label="Application Type"
                    value={props.type}
                    name="type"
                    options={applicationTypeOpts}
                    onChange={handleChange}
                ></Select>
            </Col>
            <Col s={4} m={6} l={3} className="mn-radio">
                <Select
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
                    label="Micronaut Version"
                    id="version"
                    name="version"
                    value="2.0.0.M3"
                    onChange={FAKE_ChangeHandler}
                    options={MICRONAUT_VERSIONS}
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
