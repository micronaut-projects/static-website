// TwitterLink.js
import React from "react";

export const TwitterLink = ({ className }) => {
    return (
        <a
            href="https://twitter.com/micronautfw"
            target="_blank"
            rel="noopener noreferrer"
            className={className}
        >
            <img
                src="https://image.flaticon.com/icons/png/512/23/23931.png"
                alt="Twitter"
                rel="noopener noreferrer"
                height="30px"
                weight="30px"
            />
        </a>
    );
};
export default TwitterLink;
