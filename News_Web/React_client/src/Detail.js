import React, {Component} from "react";
import {Card, Image} from "react-bootstrap";
import {FaRegBookmark, FaBookmark} from 'react-icons/fa';
import {MdKeyboardArrowDown, MdKeyboardArrowUp} from 'react-icons/md';
import {
    EmailIcon,
    EmailShareButton,
    FacebookIcon,
    FacebookShareButton,
    TwitterIcon,
    TwitterShareButton
} from "react-share";
import {IconContext} from "react-icons";
import "./Detail.css";
import "./SearchLoader.css";
import BounceLoader from "react-spinners/BounceLoader";
import ReactTooltip from 'react-tooltip';
import {ToastContainer, toast, cssTransition} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import CommentBox from "./CommentBox";
import "./Zoom.css";

import {css} from 'glamor';

const url = require("url");

const Zoom = cssTransition({
    enter: 'zoomIn',
    exit: 'zoomOut',
    // default to 750ms, can be omitted
    duration: 750,
});

var Scroll = require('react-scroll');
var Element = Scroll.Element;
var scroller = Scroll.scroller;
var scroll = Scroll.animateScroll;

class Detail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true,
            hasMore: false,
            des: [],
            showMore: false,
            showLess: false,
            bookmark: false,
        };

        this.myRef = React.createRef();

    }



    componentDidMount() {

        let uri;
        const query = url.parse(this.props.location.search, true).query;

        if(query.source != null){
            if(query.source === "nyt"){
                // uri = "/nytDetail/"
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/nytDetail/"
            }
            else{
                // uri = "/gdDetail/";
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/gdDetail/";
            }
        }
        else {
            if (localStorage.getItem('switch') != null && localStorage.getItem('switch') === "false") {
                // uri = "/nytDetail/";
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/nytDetail/";
            } else {
                // uri = "/gdDetail/";
                uri = "http://ec2-3-89-133-193.compute-1.amazonaws.com:4000/gdDetail/";
            }
        }
        console.log(this.props, query, query.id);
        fetch(uri + this.props.location.search)
            .then(response => response.json())
            .then(data => {
                let des = data.description.split(/[.+?!] /);
                let hasMore = des.length > 4;
                this.setState({
                    data: data,
                    des: des,
                    loading: false,
                    hasMore: hasMore,
                    showMore: hasMore,
                    bookmark: localStorage.getItem(data.id) ? true : false
                })
            });


    }




    handleShowMore = () => {

        this.setState({
            hasMore: false,
            showMore: false,
            showLess: true
        });
        // this.myRef.current.scrollIntoView({behavior: 'smooth'});
        scroller.scrollTo('underRef', {
            duration: 1000,
            smooth: true

        })



    };

    handleShowLess = () => {
        // let scroll = Scroll.animateScroll;
        // scroll.scrollToTop("topPosition", {smooth: true});
        this.setState({
            hasMore: true,
            showLess: false,
            showMore: true
        });
        // scroller.scrollTo('topRef', {
        //     duration: 10,
        //     smooth: true
        //
        // })
        scroll.scrollTo(0, {
            duration: 400,
            smooth: true
        });

        // window.scrollTo(0,0);
        // window.scrollTo({top: 0, behavior: 'smooth'});

    };

    addBookmark = () => {
        const data = this.state.data;
        let section2;
        if (localStorage.getItem('switch') != null && localStorage.getItem('switch') === "false") {
            section2 = "NYTIMES";
        } else {
            section2 = "GUARDIAN"
        }
        const bmInfo = {
            id: data.id,
            title: data.title,
            image: data.image,
            date: data.date,
            section: data.section,
            section2: section2,
            shareUrl: data.shareUrl
        };
        localStorage.setItem(bmInfo.id, JSON.stringify(bmInfo));

        toast(
            "Saving " + this.state.data.title,
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
        this.setState({
            bookmark: true
        })

    };

    rmBookmark = () => {
        localStorage.removeItem(this.state.data.id);
        toast(
            "Removing " + this.state.data.title,
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
        this.setState({
            bookmark: false
        });


    };

    render() {

        const data = this.state.data;
        let des = this.state.des;
        let description = "";
        let des_under = "";
        if(des.length > 4){

            for (let i = 0; i < 4; i++) {
                description = description + des[i] + ". ";
            }

            for(let i = 4; i < des.length - 1; i++){
                des_under = des_under + des[i] + ". ";
            }
            des_under += des[des.length - 1];
        }
        else{
            for (let i = 0; i < des.length - 1; i++) {
                description = description + des[i] + ". ";
            }
            description += des[des.length - 1];
        }


        if (this.state.loading === true) {
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
                </div>
            )
        } else {
            return (
                <div>
                    <ToastContainer/>
                    <Card className="detail">
                        <Card.Body>
                            <Element name="topRef"/>
                            <div className="detailCon" >
                                <Card.Title className="detailTitle" name="topPosition">
                                    {data.title}
                                </Card.Title>

                            <p className="detailDate">{data.date}</p>

                            <div className="detailIcon">
                                <FacebookShareButton url={data.shareUrl} hashtag={"#CSCI_571_NewsApp"}>
                                    <FacebookIcon size={30} round data-tip='Facebook' data-for="dt"/>
                                </FacebookShareButton>

                                <TwitterShareButton url={data.shareUrl} hashtags={["CSCI_571_NewsApp"]}>
                                    <TwitterIcon size={30} round data-tip='Twitter' data-for="dt"/>
                                </TwitterShareButton>
                                <EmailShareButton url={data.shareUrl} subject={"#CSCI_571_NewsApp"}>
                                    <EmailIcon size={30} round data-tip='Email' data-for="dt"/>
                                </EmailShareButton>


                                <div
                                    className="detailBookmark"
                                    style={{display: this.state.bookmark ? "none" : "inline-block"}}
                                >
                                    <IconContext.Provider value={{color: "red", size: "1.5em"}}>

                                        <FaRegBookmark data-tip="Bookmark" data-for="dt" onClick={this.addBookmark}/>


                                    </IconContext.Provider>
                                </div>

                                <div
                                    className="detailBookmark"
                                    style={{display: this.state.bookmark ? "inline-block" : "none"}}
                                >
                                    <IconContext.Provider value={{color: "red", size: "1.5em"}}>
                                        <FaBookmark data-tip="Bookmark" data-for="dt" onClick={this.rmBookmark}/>


                                    </IconContext.Provider>
                                </div>

                                <ReactTooltip effect="solid" id="dt"/>
                            </div>

                            <Image src={data.image} style={{width: "100%"}}/>

                            <Card.Text className="detailDes">

                                <span className="desTop"> {description} </span>
                                {/*<span style={{display: this.state.hasMore ? "inline-block" : "none"}}> {"..."} </span>*/}
                                <span
                                    style={{display: this.state.hasMore ? "none" : "inline-block"}}
                                    className="desUnder"
                                >
                                    {des_under}
                                </span>
                                {/*<span ref={this.myRef}> </span>*/}
                                <Element name="underRef"/>

                            </Card.Text>
                            </div>

                            <IconContext.Provider value={{color: "black", size: "2em"}}>

                                <div className="moreArrow"

                                     style={{display: this.state.showMore ? "block" : "none"}}
                                >
                                    <MdKeyboardArrowDown onClick={this.handleShowMore}/>
                                </div>

                            </IconContext.Provider>
                            <IconContext.Provider value={{color: "black", size: "2em"}}>

                                <div
                                    className="lessArrow"
                                    onClick={this.handleShowLess}
                                    style={{display: this.state.showLess ? "block" : "none"}}
                                >
                                    <MdKeyboardArrowUp/>
                                </div>

                            </IconContext.Provider>
                        </Card.Body>
                    </Card>

                    <CommentBox id={this.state.data.shareUrl}/>

                </div>
            );
        }
    }
}

export default Detail