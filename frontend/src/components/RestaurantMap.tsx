"use client";

import { MapContainer, Marker, Popup, TileLayer, useMap, useMapEvents } from "react-leaflet";
import L from "leaflet";
import Link from "next/link";
import { useEffect } from "react";
import type { RestaurantSummary } from "@/lib/types";
import { Stars } from "./ui";

/**
 * Map pin.
 *
 * <p>Leaflet's default marker is a blue PNG fetched from unpkg at runtime — a
 * third-party request on every map view, in a colour that belongs to no part of
 * this design. This is an inline SVG `divIcon` in the brand red instead: no
 * network request, crisp at any density, and it matches the rest of the site.
 */
const icon = L.divIcon({
  className: "", // Leaflet's own class adds a white box; the SVG is the marker.
  html: `<svg viewBox="0 0 24 32" width="32" height="42" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
    <path d="M12 0C5.4 0 0 5.3 0 11.8 0 20.6 12 32 12 32s12-11.4 12-20.2C24 5.3 18.6 0 12 0Z" fill="#ff3d1c"/>
    <circle cx="12" cy="11.6" r="4.4" fill="#fff"/>
  </svg>`,
  // 32x42 rather than Leaflet's 25x41 default: a pin is a tap target, and
  // 26px wide was under the comfortable minimum on a phone.
  iconSize: [32, 42],
  iconAnchor: [16, 42],
  popupAnchor: [0, -38],
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
  className = "h-[70vh] w-full",
}: {
  restaurants: RestaurantSummary[];
  centre: [number, number] | null;
  onBoundsChange: (bounds: Bounds) => void;
  className?: string;
}) {
  return (
    <MapContainer
      center={centre ?? [25.0478, 121.517]}
      zoom={14}
      scrollWheelZoom
      className={className}
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
          title={restaurant.name}
        >
          <Popup>
            <div className="min-w-44">
              <Link
                href={`/restaurants/${restaurant.id}`}
                className="font-display text-[15px] font-bold text-ink hover:text-pepper-ink hover:underline"
              >
                {restaurant.name}
              </Link>
              <p className="mt-1 text-xs leading-relaxed text-body">{restaurant.address}</p>
              <div className="mt-2">
                <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
              </div>
              {restaurant.distanceMetres != null && restaurant.distanceMetres > 0 && (
                <p className="mt-1.5 text-xs tabular text-subtle">
                  約 {Math.round(restaurant.distanceMetres)} 公尺
                </p>
              )}
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}
