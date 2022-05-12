(ns dev
  (:require
   [clojure.tools.namespace.repl :refer [refresh]]
   [clojure.java.io :as io]
   [clojure.data.json :as json]

   [org.httpkit.client :as http]
   [hiccup.core :as hiccup]

   [literate.client.core :as l]))

;; Use default client.
(def l (partial l/transact nil))

(comment

  (refresh)


  ;; -- Vega Lite.

  (l
    (l/vega-lite
      {:description "A simple bar chart with embedded data."
       :data {:values
              [{:a "A" :b 28}
               {:a "B" :b 55}
               {:a "C" :b 43}
               {:a "D" :b 91}
               {:a "E" :b 81}
               {:a "F" :b 53}
               {:a "G" :b 19}
               {:a "H" :b 87}
               {:a "I" :b 52}]}
       :mark "bar"
       :encoding {:x {:field "a"
                      :type "ordinal"}
                  :y {:field "b"
                      :type "quantitative"}}}))

  ;; -- Codemirror.

  (l (l/codemirror (slurp (io/resource "literate/client/core.clj"))))


  ;; -- Markdown.

  (l (l/markdown "# Welcome to Literate\n\nEval some forms to get started!"))


  ;; -- HTML.

  (l
    (l/html
      (hiccup/html
        [:div.bg-white.p-3
         [:h1.text-6xl "Hello from Hiccup"]
         [:span "Text"]])))


  ;; -- Row.

  (l
    (l/row
      {}
      (l/codemirror (slurp (io/resource "literate/client/core.clj")))
      (l/vega-lite
        {:description "A simple bar chart with embedded data."
         :data {:url "https://vega.github.io/editor/data/stocks.csv"}
         :transform [{"filter" "datum.symbol==='GOOG'"}]
         :mark "line"
         :encoding {:x {:field "date"
                        :type "temporal"}
                    :y {:field "price"
                        :type "quantitative"}}})))


  ;; -- Column.

  (l
    (l/column
      {}
      (l/codemirror (slurp (io/resource "literate/client/core.clj")))
      (l/vega-lite
        {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
         :description "A simple bar chart with embedded data."
         :data {:url "https://vega.github.io/editor/data/stocks.csv"}
         :transform [{"filter" "datum.symbol==='GOOG'"}]
         :mark "line"
         :encoding {:x {:field "date"
                        :type "temporal"}
                    :y {:field "price"
                        :type "quantitative"}}})))


  ;; -- Welcome.

  (l
    (l/hiccup
      [:div.flex.flex-col.space-y-3.bg-white.p-3.font-light
       [:h1.text-3xl
        {:style {:font-family "Cinzel"}}
        "Welcome to Literate"]

       [:p.font-semibold
        "Literate is a Clojure & ClojureScript application which you can use to visualize data."]

       [:p.mt-4
        "This interface that you're looking at it's called a " [:span.font-bold "Widget"]
        ", and you can create one from a Clojure REPL."]

       [:p.mt-2.mb1 "There are a few different types of Widgets that are supported:"]

       [:ul.list-disc.list-inside.ml-2
        [:li "Code"]
        [:li "Markdown"]
        [:li "Hiccup"]
        [:li "Vega Lite"]
        [:li "Leaflet"]
        [:li "Column layout"]
        [:li "Row layout"]]])

    (l/hiccup
      [:span.p-2.text-lg "Codemirror Widget"])

    (l/codemirror (slurp (io/resource "literate/client/core.clj")))


    (l/hiccup
      [:span.p-2.text-lg "Vega Lite Widget"])

    (l/vega-lite
      {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
       :description "A simple bar chart with embedded data."
       :data {:url "https://vega.github.io/editor/data/stocks.csv"}
       :transform [{"filter" "datum.symbol==='GOOG'"}]
       :mark "line"
       :encoding {:x {:field "date"
                      :type "temporal"}
                  :y {:field "price"
                      :type "quantitative"}}}))

  )
