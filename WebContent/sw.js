// VERSION: 0.5
// 1. Save the files to the user's device
// The "install" event is called when the ServiceWorker starts up.
// All ServiceWorker code must be inside events.
var dataCacheName = 'cliquemedicos-v1';
var cacheName = 'cliquemedicos-v2';
self.addEventListener('install', function (e) {
  //console.log('install');

  // waitUntil tells the browser that the install event is not finished until we have
  // cached all of our files
  e.waitUntil(
    // Here we call our cache "A7 - Hepta Tecnologia", but you can name it anything unique
    caches.open(cacheName).then(cache => {
      // If the request for any of these resources fails, _none_ of the resources will be
      // added to the cache.
      return cache.addAll([]);
    })
  );
});
self.addEventListener('activate', function (e) {
  //console.log('[ServiceWorker] Activate');
  /*
  e.waitUntil(
    caches.keys().then(function (keyList) {
      return Promise.all(keyList.map(function (key) {
        if (key !== cacheName && key !== dataCacheName) {
          console.log('[ServiceWorker] Removing old cache', key);
          return caches.delete(key);
        }
      }));
    })
  );
  */
});

self.addEventListener('fetch', function (e) {
  //console.log('[Service Worker] Fetch', e.request.url);
  /*
  e.respondWith(
    caches.open(cacheName).then(function (cache) {
      return cache.match(e.request).then(function (response) {
        return response || fetch(e.request).catch();
      })
    })
    );
    */
  });
  