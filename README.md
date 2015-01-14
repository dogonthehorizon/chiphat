# chiphat ![](https://circleci.com/gh/dogonthehorizon/chiphat/tree/master.png?circle-token=:circle-token&style=shield)

A Clojure library for accessing the HipChat v2 API.

### Implementation Progress
This README is updated with the implementation of each new endpoint!

* Emoticons **2/2**
* Rooms **13/24**
* Users **1/13**
* OAuth Sessions **0/3**

Also on the list of planned features:

* Rate-limit detection and handling
* Title expansion where supported in the HipChat api.

## Usage

### Getting Started

#### Leiningen

Add the following to the `:dependencies` block of your `project.clj`

[![Clojars Project](http://clojars.org/chiphat/latest-version.svg)](http://clojars.org/chiphat)

Alternatively, you can require chiphat from the REPL like so:

```clojure
(require '[chiphat.core :as ch]
         '[chiphat.rooms :as room])
;; etc.
```

### Authentication

HipChat uses a token based authentication approach for their api. Before
attempting to use this library please make sure you have your api token (You
can get one by [going here][token]).

With your api token in hand, you may authenticate all future requests by
calling `set-token!` in the main namespace of your application.

```clojure
(ns sample.app.core
  (:require [chiphat.core :as hc]))

(hc/set-token! "your-api-token-here")
```

Alternatively, if you'd like to authenticate only a few api calls, you may do
so with the `with-token` macro.

```clojure
(ns sample.app.core
  (:require [chiphat.core :as hc]))

(hc/with-token (atom "your-api-token-here")
  (println "your api requests here"))
```

### Interacting with the HipChat API

#### Simple requests
Once authenticated, you can make requests to HipChat by calling the appropriate
method for your needs.

Required parameters for a request are passed in to each function in order. Say
for example you wanted to create a room, you would pass in the room name
directly to the function like so:

```clojure
(room/create "my room")
;;=> #<core$promise$reify__6709@33f0df40: :pending>
```

But what if you wanted to give that room a topic name as well? In these
situations an optional map is passed in to the function:

```clojure
(room/create "my room" {:topic "a place for me to discuss things."
                        :guest_access true})
;;=> #<core$promise$reify__6709@738a2911: :pending>
```

#### Getting values out of a response

All requests in the Chiphat library are implemented using http-kit. To provide
flexibility to the developer, simple requests are not destructured but instead
returned as promises. To get a response from your requests, a convenience
function is provided to get a map of the response.

```clojure
(ns sample.app.core
  (:use clojure.pprint)
  (:require [chiphat.core :as hc]
            [chiphat.rooms :as room]))

(hc/with-token (atom "your api token")
  (let [my-room (room/create "my room")]
    (pprint (hc/parse-response my-room))))
;;=> {:id 977342,
;;    :links {:self "https://api.hipchat.com/v2/room/977342"}}
```

## Tests

Tests are implemented project-wide with the [clojure.test][test] runner and can
be run with `lein test` (or from the REPL with something like
`(clojure.test/run-tests 'chiphat.core-test)`)

## Contributing

This project is BSD licensed, so, contribute away!

Examples of contributions include:
* Documentation additions, corrections, and improvements
* Code for new features, bug fixes, etc
* Bug reports

The preferred method for contribution is to [fork this repository][fork] and
make your changes in a new branch. Branch names should be descriptive of the
changes that you are implementing.

When you feel that your changes are ready to be merged, make a [Pull Request][pr]
to the `master` branch.

**Note: please do not submit pull requests that implement code changes without
first updating the relevant tests!**

## License

Copyright © 2014-2015 Fernando Freire

Distributed under the BSD-3 License.

[token]: https://www.hipchat.com/account/api
[fork]: https://github.com/dogonthehorizon/chiphat/fork
[pr]: https://help.github.com/articles/using-pull-requests/
[test]: https://clojure.github.io/clojure/clojure.test-api.html
