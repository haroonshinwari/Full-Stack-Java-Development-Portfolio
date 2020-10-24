import React, { Component } from "react";
import "./css/Change.css";

class Change extends Component {
    changeToString = change => {
        let changeString = "";
        for (let coin in change) {
            if (change[coin] !== 0) {
                changeString += change[coin] + " " + coin + ", ";
            }
        }
        changeString = changeString.length > 0 ? changeString.slice(0, changeString.length - 2) : "";
        return changeString;
    };

    render() {
        let changeString = this.changeToString(this.props.change);
        return (
            <div className="changeForm">
                <div id="changeFormTitle">Change</div>

                <div id="displayChangeBox">{changeString}</div>
            </div>
        );
    }
}

export default Change;
