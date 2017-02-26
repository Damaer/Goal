import React, {Component} from 'react'
// material ui
import TextField from 'material-ui/TextField'
import Avatar from 'material-ui/Avatar'

class SentenceForm extends Component {
	render() {
		const {sentence, backImg} = this.props;
		const {handleChange} = this.props;
		return (
			<form>
				<TextField
					floatingLabelText="Sentence"
					fullWidth={true}
					value={sentence}
					data-name="sentence"
					onChange={handleChange}
				/>
				<TextField
					floatingLabelText="BackImg"
					fullWidth={true}
					value={backImg}
					data-name="backImg"
					onChange={handleChange}
				/>
			</form>
		)
	}
}

export default SentenceForm