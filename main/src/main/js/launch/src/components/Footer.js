// Footer.js
import React from "react";
import InfoButton from "./InfoButton";
import GitHubLink from "./GitHubLink";
import TwitterLink from "./TwitterLink";

import { Button } from "react-materialize";
import Icon from "react-materialize/lib/Icon";

const Footer = ({ info, theme, onToggleTheme, onShowInfo }) => {
    return (
        <div className="mn-footer">
            <InfoButton theme={theme} />

            <Button
                floating
                className={theme}
                onClick={onToggleTheme}
                style={{ marginLeft: "5px" }}
            >
                <Icon>brightness_medium</Icon>
            </Button>

            <TwitterLink theme={theme} className="mn-footer-logos" />
            <GitHubLink theme={theme} className="mn-footer-logos" />
        </div>
    );
};

export default Footer;
