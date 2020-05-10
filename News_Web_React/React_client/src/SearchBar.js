import React, {Component} from "react";
import AsyncSelect from 'react-select/lib/Async';
import {withRouter} from "react-router-dom";

const handleSearchChange = (inputValue) => {
        return fetch(
            "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?q="+inputValue,
            {
                headers: {
                    "Ocp-Apim-Subscription-Key": "9cf9e0738fe34e33aedde1e9e0703394"
                }
            }
        ).then(response => response.json())
            .then(raw => raw.suggestionGroups[0].searchSuggestions)
            .then(results => results.map(result => ({ label: result.displayText, value: result.displayText })))


};

class SearchBar extends Component{
    constructor(props) {
        super(props);
        this.state = {
            inputValue: "",
            value: null
        }
    }
    componentDidMount(): void {
        if(this.props.location.pathname === "/search"){
            this.setState({
                value: { label: this.props.location.search.substring(3), value: this.props.location.search.substring(3) }
            })
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.location.pathname !== prevProps.location.pathname && this.props.location.pathname !== "/search") {
            if(this.state.value != null) {
                this.setState({
                    value: null
                })
            }
        }
    }

    handleChange = (opt) => {
        this.setState({
            value: opt
        });
        if(!opt){
            return;
        }
        if(!opt.value){
            return;
        }

        this.props.history.push("/search?q="+opt.value)

    };





    render() {

        return (
        <div className="inputPlace" style={{width: "250px"}}>
            <AsyncSelect
                value={this.state.value}
                placeholder="Enter Keyword .."
                loadOptions={handleSearchChange}
                onChange={this.handleChange}
                noOptionsMessage={()=> "No Match"}
            />
        </div>
        );
    }
}

export default withRouter(SearchBar)