import React, {Component} from "react";
import {Container, Row} from "react-bootstrap";
import ShortCard from "./ShortCard";
import BounceLoader from "react-spinners/BounceLoader";
import "./SearchLoader.css";

class SearchLoader extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            data: []
        }

    }

    componentDidMount() {

        let uri;
        if(localStorage.getItem('switch') != null && localStorage.getItem('switch') === "false"){
            // uri = "/nytSearch/";
            uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/nytSearch/";
        }
        else {
            // uri = "/gdSearch/"
            uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/gdSearch/"
        }
        fetch(uri + this.props.location.search)
            .then(response => response.json())
            .then(data => {
                this.setState({
                    loading: false,
                    data: data
                })
            })

    }

    componentDidUpdate(prevProps, prevState, snapshot) {

        if (prevProps.location.key !== this.props.location.key) {
            this.setState({
                loading: true
            });
            let uri;
            if(localStorage.getItem('switch') != null && localStorage.getItem('switch') === "false"){
                // uri = "/nytSearch/";
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/nytSearch/";
            }
            else {
                // uri = "/gdSearch/"
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/gdSearch/"
            }
            fetch(uri + this.props.location.search)
                .then(response => response.json())
                .then(data => {
                    this.setState({
                        loading: false,
                        data: data
                    })
                })
        }
    }


    render() {
        const cardComponent = this.state.data.map(
            card => <ShortCard
                key={card.key}
                id={card.id}
                title={card.title}
                image={card.image}
                section={card.section}
                date={card.date}
                shareUrl = {card.shareUrl}
            />
        );
        if(this.state.loading === true){
            if(window.screen.width < 800){
                return(
                    <div>

                    </div>
                )
            }
            else return (
                <div style={{
                    transform: "translate(-50%, -50%)",
                    left: "50%",
                    top: "50%",
                    position: "absolute"
                }}>

                    <div className="loader">
                    <BounceLoader
                        size={50}
                        color={"rgb(95, 121, 207)"}
                    />
                    </div>
                    <p ><b className="load">Loading</b></p>
                </div>)
        }
        else {
            return (
                <Container fluid>
                    <Row>
                        <p className="searchRes"><b>Results</b></p>
                    </Row>
                    <Row>
                    {cardComponent}
                    </Row>
                </Container>
            );
        }

    }

}

export default SearchLoader