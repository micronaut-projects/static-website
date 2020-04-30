// GitHubLink.js
import React from "react";
import GitHubIcon from "@material-ui/icons/GitHub";

export const GitHubLink = ({ className, theme }) => {
    return (
        <a
            href="https://github.com/micronaut-projects"
            target="_blank"
            rel="noopener noreferrer"
            className={className}
        >
            <GitHubIcon style={{ fontSize: 32 }} />
        </a>
    );
};
export default GitHubLink;
