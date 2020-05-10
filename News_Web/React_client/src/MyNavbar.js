import React, {Component} from "react";
import {Navbar, Nav} from "react-bootstrap";
import Switch from "react-switch";
import {FaRegBookmark, FaBookmark} from 'react-icons/fa';
import {IconContext} from "react-icons";
import {NavLink, withRouter} from "react-router-dom";
import ReactTooltip from 'react-tooltip';
import SearchBar from "./SearchBar";
import "./MyNavbar.css";

class MyNavbar extends Component {
    constructor(props) {
        super(props);
        let swi = true;
        if (localStorage.getItem('switch') != null) {
            if (localStorage.getItem('switch') === "false") {
                swi = false;

            }
        }
        this.state = {
            switch: swi,
            results:[],
            selectedResult: null,
            bookmark: false,
            value: null
        }

    }



    handleChange = (checked) => {
        this.setState({
            switch: checked
        });
        localStorage.setItem('switch', checked ? "true" : "false");
        window.location.reload();

    };

    showBookmark = () => {
        this.props.history.push("/favorites");
        this.setState({
            bookmark: true
        })
    };



    render() {
        return (
            <Navbar className="myNav" expand="lg" collapseOnSelect variant="dark">
                {/*<SearchBar value={this.state.value}/>*/}
                <SearchBar/>

                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <Nav.Link as={NavLink} to="/" exact> Home </Nav.Link>
                        <Nav.Link as={NavLink} to='/world'> World </Nav.Link>
                        <Nav.Link as={NavLink} to='/politics'> Politics </Nav.Link>
                        <Nav.Link as={NavLink} to='/business'> Business </Nav.Link>
                        <Nav.Link as={NavLink} to='/technology'> Technology </Nav.Link>
                        <Nav.Link as={NavLink} to='/sports'> Sports </Nav.Link>

                    </Nav>

                    <Nav>

                        <IconContext.Provider value={{color: "white", size: "1.5em"}}>
                            <div className="bookmark" style={{display: this.props.location.pathname === "/favorites" ? "none" : "block"}}>
                                <FaRegBookmark data-tip="Bookmark" onClick={this.showBookmark} data-for="bm"/>

                            </div>
                        </IconContext.Provider>

                        <IconContext.Provider value={{color: "white", size: "1.5em"}}>
                            <div className="bookmark" style={{display: this.props.location.pathname === "/favorites" ? "block" : "none"}}>
                                <FaBookmark data-tip="Bookmark" data-for="bm"/>
                            </div>
                        </IconContext.Provider>
                        <ReactTooltip effect="solid" place="bottom" id="bm"/>


                            <span className='nyTxt'
                                  style={{display: this.props.location.pathname === "/detail"
                                      || this.props.location.pathname === "/search"
                                      ||  this.props.location.pathname === "/favorites"
                                          ? "none" : "block"}}
                            >
                                NYTimes
                            </span>
                            <div className="switchButton"
                                 style={{display: this.props.location.pathname === "/detail"
                                     || this.props.location.pathname === "/search"
                                     ||  this.props.location.pathname === "/favorites"
                                         ? "none" : "block"}}
                            >
                                <Switch
                                    checkedIcon={false}
                                    uncheckedIcon={false}
                                    offColor='#777'
                                    onColor='#028efb'
                                    name="switch"
                                    activeBoxShadow='0 0 0 0'
                                    onChange={this.handleChange}
                                    checked={this.state.switch}/>
                            </div>
                            <span className='gTxt'
                                  style={{display: this.props.location.pathname === "/detail"
                                      || this.props.location.pathname === "/search"
                                      ||  this.props.location.pathname === "/favorites"
                                          ? "none" : "block"}}
                            >
                                Guardian
                            </span>

                    </Nav>

                </Navbar.Collapse>
            </Navbar>
        )
    }
}

export default withRouter(MyNavbar)