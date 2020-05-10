import React from 'react';
import {Route, Switch} from "react-router-dom";

import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

import MyNavbar from "./MyNavbar"
import CardLoader from "./CardLoader";
import Detail from "./Detail";
import SearchLoader from "./SearchLoader";
import BookmarkLoader from "./BookmarkLoader";

function App() {
  return (
    <div className="App">
        <MyNavbar/>

        <main>
            <Switch>
                <Route path="/" component={CardLoader} exact/>
                <Route path="/world" component={CardLoader} />
                <Route path="/politics" component={CardLoader} />
                <Route path="/business" component={CardLoader} />
                <Route path="/technology" component={CardLoader} />
                <Route path="/sports" component={CardLoader} />
                <Route path="/detail" component={Detail} />
                <Route path="/search" component={SearchLoader}/>
                <Route path="/favorites" component={BookmarkLoader}/>

            </Switch>
        </main>
    </div>
  );
}

export default App;
