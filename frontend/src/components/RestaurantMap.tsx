"use client";

import { MapContainer, Marker, Popup, TileLayer, useMap, useMapEvents } from "react-leaflet";
import L from "leaflet";
import Link from "next/link";
import { useEffect } from "react";
import type { RestaurantSummary } from "@/lib/types";
import { Stars } from "./ui";

// Leaflet's default marker images resolve relative to the CSS file, which does
// not survive bundling. Point them at the CDN copies explicitly.
const icon = L.icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
});

export interface Bounds {
  south: number;
  west: number;
  north: number;
  east: number;
}

function BoundsWatcher({ onChange }: { onChange: (bounds: Bounds) => void }) {
  const map = useMapEvents({
    moveend: () => emit(),
    zoomend: () => emit(),
  });

  function emit() {
    const b = map.getBounds();
    onChange({
      south: b.getSouth(),
      west: b.getWest(),
      north: b.getNorth(),
      east: b.getEast(),
    });
  }

  useEffect(emit, []); // eslint-disable-line react-hooks/exhaustive-deps
  return null;
}

function Recentre({ centre }: { centre: [number, number] | null }) {
  const map = useMap();
  useEffect(() => {
    if (centre) map.flyTo(centre, 15);
  }, [centre, map]);
  return null;
}

export function RestaurantMap({
  restaurants,
  centre,
  onBoundsChange,
}: {
  restaurants: RestaurantSummary[];
  centre: [number, number] | null;
  onBoundsChange: (bounds: Bounds) => void;
}) {
  return (
    <MapContainer
      center={centre ?? [25.0478, 121.517]}
      zoom={14}
      scrollWheelZoom
      className="h-[70vh] w-full rounded-xl"
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <BoundsWatcher onChange={onBoundsChange} />
      <Recentre centre={centre} />

      {restaurants.map((restaurant) => (
        <Marker
          key={restaurant.id}
          position={[Number(restaurant.latitude), Number(restaurant.longitude)]}
          icon={icon}
        >
          <Popup>
            <div className="min-w-40">
              <Link href={`/restaurants/${restaurant.id}`} className="font-semibold text-red-700 hover:underline">
                {restaurant.name}
              </Link>
              <p className="mt-1 text-xs text-stone-600">{restaurant.address}</p>
              <div className="mt-1">
                <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
              </div>
              {restaurant.distanceMetres != null && restaurant.distanceMetres > 0 && (
                <p className="mt-1 text-xs text-stone-500">約 {Math.round(restaurant.distanceMetres)} 公尺</p>
              )}
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}
