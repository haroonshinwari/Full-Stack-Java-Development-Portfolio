import React, { Component } from "react";
import "./css/Item.css";

class Item extends Component {
    render() {
        return (
            <div className="items" onClick={() => this.props.onSelect(this.props.item)}>
                <span id="itemID"> {this.props.item.id}</span>
                <p id="itemName">{this.props.item.name}</p>
                <p id="itemPrice">$ {this.props.item.price}</p>
                <p id="itemQuantity">Quantity Left: {this.props.item.quantity}</p>
            </div>
        );
    }
}

export default Item;
