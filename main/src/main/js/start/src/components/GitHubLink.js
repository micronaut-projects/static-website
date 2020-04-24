// GitHubLink.js
import React from "react";

export const GitHubLink = ({ className }) => {
    return (
        <a
            href="https://github.com/micronaut-projects"
            target="_blank"
            rel="noopener noreferrer"
            className={className}
        >
            <img
                src="https://image.flaticon.com/icons/svg/25/25231.svg"
                alt="GitHub"
                rel="noopener noreferrer"
                height="30px"
                weight="30px"
            />
        </a>
    );
};
export default GitHubLink;
