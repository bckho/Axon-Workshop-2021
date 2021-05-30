import React from 'react'


function  ReplayButtons (props) {

    return <div>
            <span class="happer-blue-bold">Replay </span>
                <a href="#" onClick = {() =>  props.handle('NORMAL', 'FORWARD')}>normal</a> &nbsp;
                <a href="#" onClick = {() =>  props.handle('FAST', 'FORWARD')}>fast</a> &nbsp;
                <a href="#" onClick = {() =>  props.handle('SLOW', 'FORWARD')}>slow</a> &nbsp;
            <span class="happer-blue-bold">Replay backwards </span>
                <a href="#" onClick = {() =>  props.handle('NORMAL', 'BACKWARD')}> normal</a> &nbsp;
                <a href="#" onClick = {() =>  props.handle('FAST', 'BACKWARD')}>fast</a> &nbsp;
                <a href="#" onClick = {() =>  props.handle('SLOW', 'BACKWARD')}>slow</a> &nbsp;
            </div>
}

export default ReplayButtons