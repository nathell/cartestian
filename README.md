# cartestian

Test all the possibilities (or at least a lot of them)!

## Rationale

In the life of a developer who writes tests for their software, there are always two (or more) possibilities. In an A/B test, either your user is assigned to group A, or to group B. Either that API call succeeds, or it fails. The list goes on.

Oftentimes, we are tempted to only test the happy path. But it would be good to test all the combinations – a Cartesian product of the “dimensions”, if you will. Or at least a reasonably-sized subset of them, if the possibility space is large.

This library helps you to do just that.

## About

You can think of this library as a glorified `doseq`. Where normally you’d write:

```clojure
(doseq [test-group [:group-a :group-b]
        api-result [:success :failure]]
  (testing (str "test-group: " test-group " api-result: " api-result)
    (your-test-body)))
```

you can now use a `with-combinations` macro:

```clojure
(ns your.test
  (:require [cartestian.core]
            [cartestian.test :refer [with-combinations]])) ; need to require both namespaces

(def possibilities-space {:test-group [:group-a :group-b]
                          :api-result [:success :failure]})

(with-combinations [v possibilities-space]
  (your-test-body))
```

The behaviour is similar. On each iteration, the `v` symbol will be bound to a map describing the “current” combination (e.g., `{:test-group :group-a, :api-result :success}`). The macro will wrap the body in `testing` for your convenience.

There are two big differences, though:

1. You can build the map passed to `v` programmatically. Note how we defined `possibilities-space` outside of `with-combinations`. This would be much harder with the `doseq` approach.
2. When the size of the possibilities space exceeds a certain threshold (set to 100 by default), Cartestian will randomly sample that many combinations out of the possible space. You can override the threshold:

```clojure
(with-combinations [v possibilities-space {:count 500}] ; test up to 500 possibilities
  (your-test-body))
```

Or test all of them, no matter how many:

```clojure
(with-combinations [v possibilities-space {:count :all}] ; test all possibilities
  (your-test-body))
```

Alternatively, you can provide the space as a seq of maps having `:name` and `:dimension`. This may be easier to construct programmatically, and it also enables you to control the order of iteration:

```clojure
(with-combinations [v [{:name :test-group, :dimension [:group-a :group-b]}
                       {:name :api-result, :dimension [:success :failure]}]]
  (your-test-body))
```

At the heart of Cartestian there’s a `cartesian-product` function that provides a virtual sequence that `with-combination` iterates over; it consumes very little memory and is indexable (via `nth`) in near-constant time.

## Platforms

Cartestian supports both Clojure and ClojureScript. See `src/cartestian/example.cljc` for an example.

## Limitations

- Cartestian currently works well when the cardinality of the space of possibilities is less than a few million. Beyond that, sampling may start to take considerable time, and it will definitely _not_ work when it’s greater than 2³¹ − 1. The current sampling algorithm is Vitter’s Method A; there are plans to implement the more efficient Method D but I haven’t gotten around to this yet.
- There is as yet no support for seeding the RNG (although you may do that outside of Cartestian).
- There is as yet no support for filtering the combinations space. You can put a `when` inside of `with-combinations` but this may reduce the number of possibilities checked.

## Status

This project is experimental. Use it in tests at your peril. Nothing _should_ explode, but I can’t guarantee it.

There are no official releases (yet), but you can use this library in your `deps.edn`-based project (it’s a good idea to update the sha to the most recent commit to this repo):

```clojure
{:deps {com.github.nathell/cartestian {:git/sha "c6cb24aa8ae9e08a6f6cfccee0a606bfba965fa0"}}}
```

## License

MIT (see the file `LICENSE`).
