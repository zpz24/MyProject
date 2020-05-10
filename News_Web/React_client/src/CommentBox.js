import React from 'react';
import commentBox from 'commentbox.io';

class CommentBox extends React.Component {
    // constructor(props) {
    //     super(props);
    // }

    componentDidMount() {

        this.removeCommentBox = commentBox('5696799104827392-proj', { defaultBoxId: this.props.id });
    }

    componentWillUnmount() {

        this.removeCommentBox();
    }

    render() {

        return (
            <div className="commentbox" />
        );
    }
}

export default CommentBox