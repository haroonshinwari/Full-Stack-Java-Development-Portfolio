import React, { Component } from "react";
import "./css/Business.css";

class Business extends Component {
    handlePurchase(event, item) {
        event.preventDefault();
        this.props.onPurchaseItem(item);
    }

    render() {
        return (
            <div className="businessForm">
                <form>
                    <div id="businessFormTitle">Messages </div>
                    <input type="text" id="messageBox" name="messageBox" value={this.props.message} readonly />
                    <div>Item: </div>
                    <input
                        type="text"
                        id="productSelectedBox"
                        name="productSelected"
                        value={this.props.selectedItem.id}
                        readonly
                    />

                    <button
                        onClick={event => this.handlePurchase(event, this.props.selectedItem.id)}
                        className="btn btn-secondary btn-sm"
                    >
                        Make Purchase
                    </button>
                </form>
            </div>
        );
    }
}

export default Business;
