var indexAPI = '/indexapi';
var rootURL = "http://" + window.location.host;

console.log(rootURL)
// window.onload = function () {

var init = {};
init.items = [];
Vue.component('item', {
    template: '#item-template',
    props: {
        record: Object
    },
    filters: {
        truncate: function (v) {
            var newline = v.indexOf('\n');
            return newline > 0 ? v.slice(0, newline) : v;
        },
        formatDate: function (v) {
            console.log("Created: ", v);
            return v.replace(/T|Z/g, ' ');
        }
    },
    methods: {
        update: function (uri) {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', rootURL + uri);
            xhr.onload = function () {
                blog.root = JSON.parse(xhr.responseText);
            };
            xhr.send();
        },
        backHome: function () {
            blog.fetchData();
        },
        showAbout: function () {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', rootURL + "/about");
            xhr.onload = function () {
                blog.root = JSON.parse(xhr.responseText);
            };
            xhr.send();
        }

    }
});
// boot up the blog
var blog = new Vue({
    el: '#blog',
    data: {
        root: init
    },
    created: function () {
        console.log("Creating...");
        this.fetchData();
    },
    methods: {
        fetchData: function () {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', rootURL + indexAPI);
            xhr.onload = function () {
                self.root = JSON.parse(xhr.responseText);
                console.log(self.root.items[0].url);
            };
            xhr.send();
        }
    }
});
