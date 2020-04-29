// Header.js
import React from "react";
import GitHub from "./GitHubLink";
import Twitter from "./TwitterLink";
import logo from "../micronaut.png";

const Header = ({ props }) => {
    return (
        <div className="mn-header d-flex">
            <div className="flex mn-logo-wrapper">
                <img
                    src={logo}
                    width="50%"
                    alt="Micronaut"
                    className="mn-logo"
                />
            </div>
            <span className="flex icon-wrapper">
                <GitHub className="header-icon" />
                <Twitter className="header-icon" />
            </span>
        </div>
    );
};

export default Header;
