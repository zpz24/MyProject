import React, {Component} from "react";
import {Container, Row} from "react-bootstrap";
import BookmarkCard from "./BookmarkCard";
import "./SearchLoader.css";
import {withRouter} from "react-router-dom";
import {ToastContainer} from "react-toastify";

class BookmarkLoader extends Component{
    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
    }

    handleDelete = () => {
        let arr = [];
        let index = 0;
        for (let i = 0; i < localStorage.length; i++) {
            let key = localStorage.key(i); //获取本地存储的Key
            if(key !== "switch") {
                let value = localStorage.getItem(key);//所有value
                arr[index++] = JSON.parse(value);
            }
        }
        this.setState({
            data: arr
        })
    };

    componentDidMount() {
        let arr = [];
        let index = 0;
        for (let i = 0; i < localStorage.length; i++) {
            let key = localStorage.key(i); //获取本地存储的Key
            if(key !== "switch") {
                let value = localStorage.getItem(key);//所有value
                arr[index++] = JSON.parse(value);
            }
        }
        this.setState({
            data: arr
        })

    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if(prevProps.location.key !== this.props.location.key) {
            console.log(prevProps.location.key, this.props.location.key);
            let arr = [];
            let index = 0;
            for (let i = 0; i < localStorage.length; i++) {
                let key = localStorage.key(i);
                if(key !== "switch") {
                    let value = localStorage.getItem(key);
                    arr[index++] = JSON.parse(value);
                }
            }
            this.setState({
                data: arr
            })
        }
        // console.log(this.props);
    }

    render(){
        if(this.state.data.length < 1){
            return(
                <div>
                    <ToastContainer/>
                     <p className="noBo"><b>You have no saved articles</b></p>
                </div>
            )

        }
        else {
            const Bookmark = this.state.data.map(
                card => <BookmarkCard
                    func={this.handleDelete}
                    key={card.id}
                    id={card.id}
                    section={card.section}
                    section2={card.section2}
                    title={card.title}
                    image={card.image}
                    date={card.date}
                    shareUrl={card.shareUrl}

                />);
            return (
                <div>
                    <ToastContainer/>
                    <Container fluid>
                        <Row>
                            <p className="favorites"><b>Favorites</b></p>
                        </Row>
                        <Row>
                            {Bookmark}
                        </Row>
                    </Container>
                </div>
            )
        }
    }
}

export default withRouter(BookmarkLoader)