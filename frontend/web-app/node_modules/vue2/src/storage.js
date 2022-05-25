'use strict'

module.exports = (function() {
  // Store.js
  var store = {}
  var win = (typeof window !== 'undefined' ? window : global)
  var doc = win.document
  var localStorageName = 'localStorage'
  // var scriptTag = 'script'
  var storage
  store.disabled = false
  store.version = '1.3.20'
  store.set = function(key, value) {}
  store.get = function(key, defaultVal) {}
  store.has = function(key) {
    return store.get(key) !== undefined
  }
  store.remove = function(key) {}
  store.clear = function() {}
  store.transact = function(key, defaultVal, transactionFn) {
    if (transactionFn == null) {
      transactionFn = defaultVal
      defaultVal = null
    }
    if (defaultVal == null) {
      defaultVal = {}
    }
    var val = store.get(key, defaultVal)
    transactionFn(val)
    store.set(key, val)
  }
  store.getAll = function() {
    var ret = {}
    store.forEach(function(key, val) {
      ret[key] = val
    })
    return ret
  }
  store.forEach = function() {}
  store.serialize = function(value) {
    return JSON.stringify(value)
  }
  store.deserialize = function(value) {
    if (typeof value !== 'string') {
      return undefined
    }
    try {
      return JSON.parse(value)
    } catch (e) {
      return value || undefined
    }
  }
  function isLocalStorageNameSupported() {
    try {
      return (localStorageName in win && win[localStorageName])
    } catch (err) {
      return false
    }
  }
  if (isLocalStorageNameSupported()) {
    storage = win[localStorageName]
    store.set = function(key, val) {
      if (val === undefined) {
        return store.remove(key)
      }
      storage.setItem(key, store.serialize(val))
      return val
    }
    store.get = function(key, defaultVal) {
      var val = store.deserialize(storage.getItem(key))
      return (val === undefined ? defaultVal : val)
    }
    store.remove = function(key) { storage.removeItem(key) }
    store.clear = function() { storage.clear() }
    store.forEach = function(callback) {
      for (var i = 0; i < storage.length; i++) {
        var key = storage.key(i)
        callback(key, store.get(key))
      }
    }
  } else if (doc && doc.documentElement.addBehavior) {
    var storageOwner
    // var storageContainer
    // try {
    //   storageContainer = new ActiveXObject('htmlfile')
    //   storageContainer.open()
    //   storageContainer.write('<' + scriptTag + '>document.w=window</' + scriptTag + '><iframe src="/favicon.ico"></iframe>')
    //   storageContainer.close()
    //   storageOwner = storageContainer.w.frames[0].document
    //   storage = storageOwner.createElement('div')
    // } catch (e) {
    //   storage = doc.createElement('div')
    //   storageOwner = doc.body
    // }
    var withIEStorage = function(storeFunction) {
      return function() {
        var args = Array.prototype.slice.call(arguments, 0)
        args.unshift(storage)
        storageOwner.appendChild(storage)
        storage.addBehavior('#default#userData')
        storage.load(localStorageName)
        var result = storeFunction.apply(store, args)
        storageOwner.removeChild(storage)
        return result
      }
    }
    var forbiddenCharsRegex = new RegExp("[!\"#$%&'()*+,/\\\\:;<=>?@[\\]^`{|}~]", 'g')
    var ieKeyFix = function(key) {
      return key.replace(/^d/, '___$&').replace(forbiddenCharsRegex, '___')
    }
    store.set = withIEStorage(function(storage, key, val) {
      key = ieKeyFix(key)
      if (val === undefined) {
        return store.remove(key)
      }
      storage.setAttribute(key, store.serialize(val))
      storage.save(localStorageName)
      return val
    })
    store.get = withIEStorage(function(storage, key, defaultVal) {
      key = ieKeyFix(key)
      var val = store.deserialize(storage.getAttribute(key))
      return (val === undefined ? defaultVal : val)
    })
    store.remove = withIEStorage(function(storage, key) {
      key = ieKeyFix(key)
      storage.removeAttribute(key)
      storage.save(localStorageName)
    })
    store.clear = withIEStorage(function(storage) {
      var attributes = storage.XMLDocument.documentElement.attributes
      storage.load(localStorageName)
      for (var i = attributes.length - 1; i >= 0; i--) {
        storage.removeAttribute(attributes[i].name)
      }
      storage.save(localStorageName)
    })
    store.forEach = withIEStorage(function(storage, callback) {
      var attributes = storage.XMLDocument.documentElement.attributes
      for (var i = 0, attr; i < attributes.length; ++i) {
        attr = attributes[i]
        callback(attr.name, store.deserialize(storage.getAttribute(attr.name)))
      }
    })
  }
  try {
    var testKey = '__storejs__'
    store.set(testKey, testKey)
    if (store.get(testKey) !== testKey) { store.disabled = true }
    store.remove(testKey)
  } catch (e) {
    store.disabled = true
  }
  store.enabled = !store.disabled
  return store
}())
