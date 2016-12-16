var indexAPI = '/indexapi';
var rootURL = "http://" + window.location.host;

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
            return v.replace(/T|Z/g, ' ');
        }
    },
    methods: {
        update: function (obj) {
            window.location.hash = obj.target.hash;
            blog.fetchData();
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
            var hash = window.location.hash;
            var apiLink = indexAPI;
            if (hash.length > 2) {
                apiLink = hash.slice(1); //remove "#"
            }
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', rootURL + apiLink);
            xhr.onload = function () {
                self.root = JSON.parse(xhr.responseText);
            };
            xhr.send();
        }
    }
});
