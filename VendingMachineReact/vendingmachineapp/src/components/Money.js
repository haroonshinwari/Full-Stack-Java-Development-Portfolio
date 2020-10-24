import React, { Component } from "react";
import "./css/Money.css";
import Button from "react-bootstrap/button";
import "bootstrap/dist/css/bootstrap.min.css";

class Money extends Component {
    render() {
        return (
            <div className="moneyForm">
                <form>
                    <div id="moneyFormTitle"> Total $ in</div>

                    <div id="displayBox"> $ {this.props.balance.toFixed(2)}</div>

                    <div id="addChangeButtons">
                        <Button
                            variant="primary"
                            className="mr-3 ml-2 mt-2 mb-2"
                            onClick={() => this.props.onAddMoney(1)}
                        >
                            Add Dollar
                        </Button>

                        <Button
                            variant="primary"
                            className="ml-3, mt-2 mb-2"
                            onClick={() => this.props.onAddMoney(0.25)}
                        >
                            Add Quarter
                        </Button>
                        <br />
                        <Button variant="primary" className="mr-3 ml-2" onClick={() => this.props.onAddMoney(0.1)}>
                            Add Dime
                        </Button>

                        <Button variant="primary" className="mr-3 ml-2" onClick={() => this.props.onAddMoney(0.05)}>
                            Add Nickel
                        </Button>
                    </div>

                    <hr />
                </form>
            </div>
        );
    }
}

export default Money;
