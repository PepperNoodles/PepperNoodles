"use client";

import { useState } from "react";
import { api } from "@/lib/api";
import { Button, ErrorNote } from "./ui";
import { IconExternal } from "./icons";

interface EcpayForm {
  action: string;
  fields: Record<string, string>;
}

/**
 * Sends the buyer to 綠界 to pay.
 *
 * <p>ECPay takes an HTML form POST, not a redirect, and every field is covered
 * by a CheckMacValue the backend computes — so the form is built here from
 * exactly what the API returned and submitted untouched. Changing any value
 * client-side would invalidate the signature.
 */
export function EcpayButton({ orderId, disabled }: { orderId: number; disabled?: boolean }) {
  const [error, setError] = useState<unknown>(null);
  const [submitting, setSubmitting] = useState(false);

  async function pay() {
    setError(null);
    setSubmitting(true);
    try {
      const { action, fields } = await api.post<EcpayForm>(`/payments/ecpay/orders/${orderId}`);

      const form = document.createElement("form");
      form.method = "POST";
      form.action = action;
      form.style.display = "none";

      for (const [name, value] of Object.entries(fields)) {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = name;
        input.value = value;
        form.appendChild(input);
      }

      document.body.appendChild(form);
      form.submit();
    } catch (e) {
      setError(e);
      setSubmitting(false);
    }
  }

  return (
    <div>
      <Button onClick={pay} disabled={disabled} loading={submitting} icon={<IconExternal />}>
        前往付款
      </Button>
      <div className="mt-2">
        <ErrorNote error={error} />
      </div>
    </div>
  );
}
