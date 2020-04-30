// Footer.js
import React from "react";
import InfoButton from "./InfoButton";

import { Button } from "react-materialize";
import Icon from "react-materialize/lib/Icon";

const Footer = ({ info, theme, onToggleTheme, onShowInfo }) => {
    return (
        <div className="mn-footer hide-on-med-and-up">
            <InfoButton theme={theme} className={`${theme} mn-footer-logos`} />

            <Button
                floating
                className={`${theme} mn-footer-logos`}
                onClick={onToggleTheme}
                style={{ marginLeft: "5px" }}
            >
                <Icon>brightness_medium</Icon>
            </Button>
        </div>
    );
};

export default Footer;
