// FeatureSelector.js
import React, { useState, useMemo, useRef, useEffect } from "react";
import ReactDOM from "react-dom";

import { Button, Card } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Icon from "react-materialize/lib/Icon";

import Preloader from "react-materialize/lib/Preloader";
import Row from "react-materialize/lib/Row";
import TextInput from "../TextInput";
import "./feature-selector.css";

const measureElement = (element) => {
    const DOMNode = ReactDOM.findDOMNode(element);
    return {
        width: DOMNode && DOMNode.offsetWidth,
        height: DOMNode && DOMNode.offsetHeight,
    };
};

const FeatureSelector = ({
    features,
    selectedFeatures,
    loading,
    onAddFeature,
    onRemoveFeature,
}) => {
    const [search, setSearch] = useState("");
    const selectedFeatureValues = Object.values(selectedFeatures).reverse();
    const container = useRef();

    useEffect(() => {
        const size = measureElement(container.current);
        console.log({ size });
    }, [container, loading]);

    const availableFeatures = useMemo(() => {
        const selectedFeatureKeys = Object.keys(selectedFeatures);
        return features.filter((feature) => {
            return !selectedFeatureKeys.includes(feature.name);
        });
    }, [features, selectedFeatures]);

    const searchResults = useMemo(() => {
        if (!search.length) {
            return availableFeatures;
        }
        const lcSearch = search.toLowerCase();
        return availableFeatures.filter((feature) => {
            const { name, description } = feature;
            return (
                name.toLowerCase().includes(lcSearch) ||
                description.toLowerCase().includes(lcSearch)
            );
        });
    }, [search, availableFeatures]);

    return (
        <React.Fragment>
            {loading ? (
                <Col s={6}>
                    <Preloader active flashing={false} size="big" />
                </Col>
            ) : (
                <Col className="available-features" s={6}>
                    {searchResults.length === 0 && <p>No matching features</p>}
                    {searchResults.map((feature, i) => (
                        <Col s={12} key={i}>
                            <Card
                                className="white mn-feature-selection"
                                title={feature.name}
                                onClick={() => onAddFeature(feature)}
                            >
                                <p className="grey-text">
                                    {feature.description}
                                </p>
                            </Card>
                        </Col>
                    ))}
                </Col>
            )}
            <Col className="selected-features" s={6}>
                <Row className="sticky">
                    <TextInput
                        className="mn-input"
                        s={12}
                        label="Features"
                        placeholder="ex: cassandra"
                        value={search}
                        onChangeText={setSearch}
                    />
                    <b>Selected features ({selectedFeatureValues.length})</b>
                    {selectedFeatureValues.length === 0 && (
                        <p className="grey-text">No features selected.</p>
                    )}
                </Row>
                <Row ref={container} className="selected-features-items">
                    {selectedFeatureValues.map((feature, i) => (
                        <div className="white selected-item-row" key={i}>
                            <h6 className="grey-text">{feature.name}</h6>
                            <small className="grey-text">
                                {feature.description}
                            </small>
                            <Button
                                floating
                                small
                                className="black remove-button"
                                onClick={(e) => onRemoveFeature(e, feature, i)}
                            >
                                <Icon>close</Icon>
                            </Button>
                        </div>
                    ))}
                </Row>
            </Col>
        </React.Fragment>
    );
};

export default FeatureSelector;
