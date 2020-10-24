import React, { Component } from "react";
import Item from "./Item.js";
import Money from "./Money";
import Business from "./Business";
import Change from "./Change";
import "./css/Container.css";

const SERVER_URL = "http://tsg-vending.herokuapp.com/";

class Container extends Component {
    state = {
        items: [],
        selectedItem: {},
        balance: 0,
        message: "",
        change: {},
    };

    handleSelect = item => {
        this.setState({ selectedItem: item }, () => console.log(this.state.selectedItem));
    };

    handleAddMoney = amount => {
        this.setState({ balance: this.state.balance + amount }, () => console.log(this.state.balance));
    };

    componentDidMount() {
        fetch(SERVER_URL + "items")
            .then(response => response.json())
            .then(itemsList => this.setState({ items: itemsList }));
    }

    purchaseItem = itemID => {
        if (itemID === undefined) {
            this.setState({ message: "Please select an item" });
            return;
        }

        fetch(SERVER_URL + "money/" + this.state.balance.toFixed(2) + "/item/" + itemID, { method: "POST" })
            .then(response => {
                if (response.status === 200) {
                    response
                        .json()
                        .then(changeData => this.setState({ change: changeData, balance: 0, message: "Thank You!!!" }));
                } else {
                    response.json().then(data => this.setState({ message: data.message }));
                }
            })
            .then(
                fetch(SERVER_URL + "items")
                    .then(response => response.json())
                    .then(itemsList => this.setState({ items: itemsList }))
            );
    };

    render() {
        return (
            <div className="Container">
                <section className="itemsWindow">
                    {this.state.items.map(item => (
                        <Item key={item.id} item={item} onSelect={this.handleSelect} />
                    ))}
                </section>

                <section className="formsWindow">
                    <Money key="Money" balance={this.state.balance} onAddMoney={this.handleAddMoney} />

                    <Business
                        key="Business"
                        message={this.state.message}
                        selectedItem={this.state.selectedItem}
                        onPurchaseItem={this.purchaseItem}
                    />
                    <Change key="Change" change={this.state.change} />
                </section>
            </div>
        );
    }
}

export default Container;
