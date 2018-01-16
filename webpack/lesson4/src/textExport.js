import _ from 'lodash'

class TestExport {

    constructor (config) {
        if (!TestExport.instance) {
            TestExport.instance = this
        }
        return TestExport.instance
    }

    hello2 () {
        function component() {
            var element = document.createElement('div');

            // Lodash, currently included via a script, is required for this line to work
            element.innerHTML = _.join(['Hello', 'webpack test Export'], ' ');

            return element;
        }
        document.body.appendChild(component());
    }

}

export {TestExport}
