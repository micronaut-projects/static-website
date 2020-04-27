// FeatureSelector.js
import React, { useMemo } from "react";

import { Button, Card } from "react-materialize";
import Col from "react-materialize/lib/Col";
import Icon from "react-materialize/lib/Icon";

import Preloader from "react-materialize/lib/Preloader";
import Row from "react-materialize/lib/Row";
import "./feature-selector.css";

const FeatureSelector = ({
    features,
    selectedFeatures,
    loading,
    search,
    onAddFeature,
    onRemoveFeature,
    theme = "light",
}) => {
    const selectedFeatureValues = Object.values(selectedFeatures).reverse();

    const availableFeatures = useMemo(() => {
        const selectedFeatureKeys = Object.keys(selectedFeatures);
        return features.map((feature) => {
            return {
                ...feature,
                selected: selectedFeatureKeys.includes(feature.name),
            };
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

    const toggleFeatures = (event, feature) => {
        feature.selected
            ? onRemoveFeature(event, feature)
            : onAddFeature(feature);
    };

    return (
        <React.Fragment>
            <Col
                className={`selected-features-mobile hide-on-med-and-up bg-${theme}`}
            >
                <b>Selected features ({selectedFeatureValues.length})</b>
            </Col>
            {loading ? (
                <Col s={12} m={6}>
                    <Preloader active flashing={false} size="big" />
                </Col>
            ) : (
                <Col className="available-features" s={12} m={6}>
                    {searchResults.length === 0 && <p>No matching features</p>}
                    {searchResults.map((feature, i) => (
                        <Col s={12} key={i}>
                            <Card
                                className={`mn-feature-selection hoverable ${feature.selected &&
                                    "selected"}`}
                                title={feature.name}
                                onClick={(e) => toggleFeatures(e, feature)}
                            >
                                <p className="grey-text">
                                    {feature.description}
                                </p>
                            </Card>
                        </Col>
                    ))}
                </Col>
            )}
            <Col className="selected-features hide-on-small-only" s={12} m={6}>
                <Row className="sticky">
                    <b>Selected features ({selectedFeatureValues.length})</b>
                    {selectedFeatureValues.length === 0 && (
                        <p className="grey-text">No features selected.</p>
                    )}
                </Row>
                <Row className="selected-features-items">
                    {selectedFeatureValues.map((feature, i) => (
                        <div className="selected-item-row" key={i}>
                            <h6 className="grey-text title">{feature.name}</h6>
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
