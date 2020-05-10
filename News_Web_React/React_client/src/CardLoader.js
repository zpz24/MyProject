import React, {Component} from "react";
import LongCard from "./LongCard";
import BounceLoader from "react-spinners/BounceLoader";
import "./SearchLoader.css"

class CardLoader extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            json: []
        }

    }

    componentDidMount() {

        let uri;
        let path = this.props.location.pathname;
        if(localStorage.getItem('switch') != null && localStorage.getItem('switch') === "false"){
            // uri = "/nyt";
            uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/nyt"
        }
        else {
            // uri = "/guardian";
            uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/guardian";
            if(path === "/sports"){
                path = "/sport";
            }
        }
        if(path === "/"){
            path = "/home"
        }
        fetch(uri + path)
            .then(response => response.json())
            .then(data => {
                this.setState({
                    loading: false,
                    json: data
                })
            })

    }

    componentDidUpdate(prevProps, prevState, snapshot) {

        if (prevProps.location.key !== this.props.location.key) {
            this.setState({
                loading: true
            });
            let uri;
            let path = this.props.location.pathname;

            if(localStorage.getItem('switch') != null && localStorage.getItem('switch') === "false"){
                // uri = "/nyt";
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/nyt"

            }
            else {
                // uri = "/guardian";
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/guardian";
                if(path === "/sports"){
                    path = "/sport";
                }
            }
            if(path === "/"){
                path = "/home"
            }
            fetch(uri + path)
                .then(response => response.json())
                .then(data => {
                    this.setState({
                        loading: false,
                        json: data
                    })
                })
        }
    }

    render() {
        const cardComponent = this.state.json.map(
            card => <LongCard
                     key={card.key}
                     id={card.id}
                     title={card.title}
                     image={card.image}
                     section={card.section}
                     date={card.date}
                     description={card.description}
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
                <div  style={{
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

                    <p><b className="load">Loading</b></p>
                </div>

            )
        }
        else {
            return (
                <div>
                    {cardComponent}
                </div>
            );
        }

    }

}
export default CardLoader