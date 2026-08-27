import type { Order } from "./types";

/**
 * One vocabulary for order status.
 *
 * <p>The list page and the detail page each carried their own copy, and they
 * disagreed: one styled 待付款 amber-100, the other amber-50 with a different
 * border. Status is the single most scanned thing on an order, so it has to
 * look identical wherever it appears.
 */
export const ORDER_STATUS: Record<
  Order["status"],
  { label: string; tone: "neutral" | "success" | "warn" | "danger" }
> = {
  PENDING: { label: "待付款", tone: "warn" },
  PAID: { label: "已付款", tone: "success" },
  CANCELLED: { label: "已取消", tone: "neutral" },
  EXPIRED: { label: "已逾期", tone: "neutral" },
};
