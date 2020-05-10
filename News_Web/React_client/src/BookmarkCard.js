import React, {Component} from "react";
import {Card, Col, Container, Row, Image, Badge, Modal} from "react-bootstrap";
import {FaShareAlt} from 'react-icons/fa';
import {MdDelete} from 'react-icons/md';
import {IconContext} from "react-icons";
import {
    EmailShareButton, EmailIcon, FacebookShareButton, FacebookIcon,
    TwitterShareButton, TwitterIcon
} from "react-share";
import {ToastContainer, toast, cssTransition} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import "./Zoom.css";
import "./ShortCard.css"
import {Link, withRouter} from "react-router-dom";
import {css} from "glamor";
import Truncate from "react-truncate";

const Zoom = cssTransition({
    enter: 'zoomIn',
    exit: 'zoomOut',
    // default to 750ms, can be omitted
    duration: 750,
});
class BookmarkCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            show: false

        }
    }


    handleShare = (event) => {
        event.preventDefault();
        this.setState({
            show: true
        });

    };

    handleHide = () => {
        this.setState({
            show: false
        })
    };

    handleDel = (event) => {
        toast(
            "Removing " + this.props.title,
            {
                position: "top-center",
                transition: Zoom,
                autoClose: 2000,
                hideProgressBar: true,
                closeOnClick: false,
                pauseOnHover: true,
                draggable: false,
                className: css({
                    color: 'black'
                })
            });
        localStorage.removeItem(this.props.id);
        this.props.history.push("/favorites");
        // this.props.func();
        event.preventDefault();

    };

    render() {
        const shareUrl = this.props.shareUrl;
        let section = this.props.section;
        let section2 = this.props.section2;
        if (section.toLowerCase() === "sport") {
            section = "sports";
        }
        let bColor;
        let tColor;
        if (this.props.section.toLowerCase() === "business") {
            bColor = "rgb(2,142,251)";
            tColor = "white";
        } else if (this.props.section.toLowerCase() === "politics") {
            bColor = "rgb(0, 143, 125)";
            tColor = "white";
        } else if (this.props.section.toLowerCase() === "world") {
            bColor = "rgb(120, 60, 252)";
            tColor = "white";
        } else if (this.props.section.toLowerCase() === "technology") {
            bColor = "rgb(195, 217, 0)";
            tColor = "black";
        } else if (this.props.section.toLowerCase() === "sports" || this.props.section.toLowerCase() === "sport") {
            bColor = "rgb(255, 185, 2)";
            tColor = "black";
        } else {
            bColor = "rgb(100, 106, 113)";
            tColor = "white";
        }
        let sec2Color;
        let sec2Back;
        let source;
        if(section2 === "GUARDIAN"){
            sec2Back = "rgb(24,35,66)";
            sec2Color = "white";
            source = "guardian";
        }
        else{
            sec2Back = "rgb(217,217,217)";
            sec2Color = "black";
            source = "nyt";
        }
        const li = "/detail?id=" + this.props.id + "&source=" + source;

        return (

            <Col lg={3}>
                <Link to={li} className="shortCardLink" style={{textDecoration: "none", color: "black"}}>
                    <Card className="shortCard">
                        <Card.Body className="shortCardMsg">
                            <Card.Title className="shortCardTitle">
                                <Truncate lines={2} ellipsis={<span>...</span>}>
                                {this.props.title}
                                </Truncate>
                                <span className="shortCardShare">
                                        <IconContext.Provider value={{}}>
                                            <div>
                                                <FaShareAlt onClick={this.handleShare}/>
                                            </div>
                                        </IconContext.Provider>

                                </span>
                                <span className="shortCardShare">
                                <IconContext.Provider value={{size: "1.2em"}}>
                                    <div>
                                        <MdDelete onClick={this.handleDel}/>
                                    </div>
                                </IconContext.Provider>
                                    </span>
                            </Card.Title>

                            <Image className="shortCardImg" thumbnail
                                   src={this.props.image}/>
                            <p className="shortCardDate">{this.props.date}</p>
                            <Badge className="shortCardColorTag2"
                                   style={{
                                       background: sec2Back,
                                       color: sec2Color
                                   }}>{section2.toUpperCase()}</Badge>
                            <Badge className="shortCardColorTag"
                                   style={{
                                       background: bColor,
                                       color: tColor
                                   }}>{section.toUpperCase()}</Badge>

                        </Card.Body>
                    </Card>
                </Link>
                <Modal show={this.state.show} onHide={this.handleHide}>

                    <Modal.Header closeButton>
                        <Modal.Title>
                            <p style={{fontSize: "30px", marginBottom: "0"}}><b>{section2.toUpperCase()}</b></p>
                            {this.props.title}
                        </Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <p className="shareVia">Share via</p>
                        <Container className="shareIcon">
                            <Row>
                                <Col>
                                    <FacebookShareButton url={shareUrl} hashtag={"#CSCI_571_NewsApp"}>
                                        <FacebookIcon size={50} round/>
                                    </FacebookShareButton>
                                </Col>
                                <Col>
                                    <TwitterShareButton url={shareUrl} hashtags={["CSCI_571_NewsApp"]}>
                                        <TwitterIcon size={50} round/>
                                    </TwitterShareButton>
                                </Col>
                                <Col>
                                    <EmailShareButton url={shareUrl} subject={"#CSCI_571_NewsApp"}>
                                        <EmailIcon size={50} round/>
                                    </EmailShareButton>
                                </Col>
                            </Row>
                        </Container>
                    </Modal.Body>

                </Modal>
            </Col>



        );
    }

}

export default withRouter(BookmarkCard)

