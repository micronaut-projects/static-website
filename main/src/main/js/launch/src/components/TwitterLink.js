// TwitterLink.js
import React from "react";

import TwitterIcon from "@material-ui/icons/Twitter";
import Avatar from "@material-ui/core/Avatar";

export const TwitterLink = ({ className, theme }) => {
    const backgroundColor =
        theme === "dark" ? "var(--theme-light)" : "var(--theme-dark)";
    const color = theme === "dark" ? "var(--theme-dark)" : "var(--theme-light)";
    const size = 32;

    return (
        <a
            href="https://twitter.com/micronautfw"
            target="_blank"
            rel="noopener noreferrer"
            className={className}
        >
            <Avatar style={{ height: size, width: size, backgroundColor }}>
                <TwitterIcon style={{ color }} />
            </Avatar>
        </a>
    );
};
export default TwitterLink;
