// TwitterLink.js
import React from "react";

import twitterLight from "../twitter.png";
import twitterDark from "../twitter-white.png";

export const TwitterLink = ({ className, theme }) => {
    const src = theme === "dark" ? twitterDark : twitterLight;

    return (
        <a
            href="https://twitter.com/micronautfw"
            target="_blank"
            rel="noopener noreferrer"
            className={className}
        >
            <img
                src={src}
                alt="Twitter"
                rel="noopener noreferrer"
                height="30px"
                weight="30px"
            />
        </a>
    );
};
export default TwitterLink;
