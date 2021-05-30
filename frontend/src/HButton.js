import React from 'react';

function HButton(props) {

    return <button className="happer-block" type="submit" onClick={props.handleFunction}>{props.title}</button>
}

export default HButton