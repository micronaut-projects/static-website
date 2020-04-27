// GitHubLink.js
import React from "react";
import githubLight from "../github.png";
import githubDark from "../github-white.png";

export const GitHubLink = ({ className, theme }) => {
    const src = theme === "dark" ? githubDark : githubLight;

    return (
        <a
            href="https://github.com/micronaut-projects"
            target="_blank"
            rel="noopener noreferrer"
            className={className}
        >
            <img
                src={src}
                alt="GitHub"
                rel="noopener noreferrer"
                height="30px"
                weight="30px"
            />
        </a>
    );
};
export default GitHubLink;
