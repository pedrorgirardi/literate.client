(ns literate.client.core
  (:require [org.httpkit.client :as http]
            [cognitect.transit :as transit]
            [rum.server-render])

  (:import (java.util UUID)
           (java.io ByteArrayOutputStream)))

(defn transit-encode
  "Encode `x` in Transit-JSON.

   Returns a Transit JSON-encoded string."
  [x]
  (let [out (ByteArrayOutputStream. 4096)
        writer (transit/writer out :json)]
    (transit/write writer x)
    (.toString out)))

(defn transact
  "Sends a transact event to the client."
  [client data]
  (http/post
    (str (:url client "http://localhost:8118") "/api/v1/transact")
    {:body
     (transit-encode data)}))

(defn view [client & widgets]
  (transact client widgets))

(defn row
  "Returns a Row (horizontal) layout entity."
  [{:keys [gap]} & children]
  {:db/id -1
   :widget/uuid (str (UUID/randomUUID))
   :widget/type :widget.type/row
   :widget/children (map #(assoc % :widget/parent -1) children)})

(defn column
  "Returns a Column (vertical) layout entity."
  [{:keys [gap]} & children]
  {:db/id -1
   :widget/uuid (str (UUID/randomUUID))
   :widget/type :widget.type/column
   :widget/children (map #(assoc % :widget/parent -1) children)})

(defn vega-emded
  "Returns a Vega Embed Widget entity."
  [spec]
  {:widget/uuid (str (UUID/randomUUID))
   :widget/type :widget.type/vega-embed
   :widget.vega-embed/spec spec})

(defn vega-lite
  "Returns a Vega Embed Widget entity."
  [vega-lite-spec]
  (vega-emded (merge {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"}
                     vega-lite-spec)))

(defn codemirror
  "Returns a Codemirror Widget entity."
  [^String value & [{:keys [width height mode lineNumbers]}]]
  (merge {:widget/uuid (str (UUID/randomUUID))
          :widget/type :widget.type/codemirror
          :widget.codemirror/mode (or mode "clojure")
          :widget.codemirror/value (or value "")
          :widget.codemirror/lineNumbers (or lineNumbers false)}

         (some->> width (hash-map :widget.codemirror/width))
         (some->> height (hash-map :widget.codemirror/height))))

(defn markdown
  "Returns a Markdown Widget entity."
  [markdown]
  #:widget {:uuid (str (UUID/randomUUID))
            :type :widget.type/markdown
            :markdown markdown})

(defn html
  "Returns an Html Widget entity."
  [html]
  {:widget/uuid (str (UUID/randomUUID))
   :widget/type :widget.type/html
   :widget.html/src html})

(defn hiccup
  "Returns a Hiccup Widget entity."
  [hiccup]
  {:widget/uuid (str (UUID/randomUUID))
   :widget/type :widget.type/html
   :widget.html/src (rum.server-render/render-static-markup hiccup)})
