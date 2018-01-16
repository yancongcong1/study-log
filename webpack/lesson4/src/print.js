import _ from 'lodash'

class Test {

    constructor (config) {
        if (!Test.instance) {
            Test.instance = this
        }
        return Test.instance
    }

    hello () {
        function component() {
            var element = document.createElement('div');

            // Lodash, currently included via a script, is required for this line to work
            element.innerHTML = _.join(['Hello', 'webpack4'], ' ');

            return element;
        }
        document.body.appendChild(component());
    }

}

export default Test
