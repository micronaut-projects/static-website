// TooltipButton.js
import React from "react";
import Tooltip from "@material-ui/core/Tooltip";
import { Button } from "react-materialize";

const TooltipButton = ({ tooltip, children, ...props }) => {
    return (
        <Tooltip enterDelay={500} enterTouchDelay={300} title={tooltip}>
            <span>
                <Button {...props}>{children}</Button>
            </span>
        </Tooltip>
    );
};

export default TooltipButton;
