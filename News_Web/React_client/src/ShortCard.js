import React, {Component} from "react";
import {Card, Col, Container, Row, Image, Badge, Modal} from "react-bootstrap";
import {FaShareAlt} from 'react-icons/fa';
import {IconContext} from "react-icons";
import {
    EmailShareButton, EmailIcon, FacebookShareButton, FacebookIcon,
    TwitterShareButton, TwitterIcon
} from "react-share";

import "./ShortCard.css"
import {Link} from "react-router-dom";
import Truncate from "react-truncate";

class ShortCard extends Component {
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

    render() {
        const shareUrl = this.props.shareUrl;
        let section = this.props.section;
        if (this.props.section.toLowerCase() === "sport") {
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
        const li = "/detail?id=" + this.props.id;

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
                                </Card.Title>

                                <Image className="shortCardImg" thumbnail
                                       src={this.props.image}/>
                                <p className="shortCardDate">{this.props.date}</p>
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
                            <Modal.Title>{this.props.title}</Modal.Title>
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

export default ShortCard

