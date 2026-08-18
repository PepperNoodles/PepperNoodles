import { describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { Empty, ErrorNote, Stars, TagPill, money } from "./ui";
import { ApiError } from "@/lib/api";

describe("money", () => {
  it("formats as NT$ with thousands separators", () => {
    expect(money(1234)).toBe("NT$1,234");
    expect(money("980.00")).toBe("NT$980");
  });

  it("rounds rather than showing cents", () => {
    expect(money("120.60")).toBe("NT$121");
  });

  it("handles zero", () => {
    expect(money(0)).toBe("NT$0");
  });
});

describe("Stars", () => {
  it("says so when nothing has been rated yet", () => {
    render(<Stars average={null} count={0} />);
    expect(screen.getByText("尚無評分")).toBeInTheDocument();
  });

  /** A restaurant with reviews but no scores must not render as 0 stars. */
  it("treats a zero count as unrated even if an average is present", () => {
    render(<Stars average="4.5" count={0} />);
    expect(screen.getByText("尚無評分")).toBeInTheDocument();
  });

  it("shows the average to one decimal place and the count", () => {
    render(<Stars average="4.25" count={12} />);
    expect(screen.getByText("4.3")).toBeInTheDocument();
    expect(screen.getByText("(12)")).toBeInTheDocument();
  });
});

describe("ErrorNote", () => {
  it("renders nothing when there is no error", () => {
    const { container } = render(<ErrorNote error={null} />);
    expect(container).toBeEmptyDOMElement();
  });

  it("shows the backend's message and is announced as an alert", () => {
    render(<ErrorNote error={new ApiError(409, null, "庫存不足")} />);
    const alert = screen.getByRole("alert");
    expect(alert).toHaveTextContent("庫存不足");
  });

  it("copes with a thrown non-Error value", () => {
    render(<ErrorNote error="something broke" />);
    expect(screen.getByRole("alert")).toHaveTextContent("something broke");
  });
});

describe("TagPill and Empty", () => {
  it("render their children", () => {
    render(
      <>
        <TagPill>小籠包</TagPill>
        <Empty>沒有資料</Empty>
      </>,
    );
    expect(screen.getByText("小籠包")).toBeInTheDocument();
    expect(screen.getByText("沒有資料")).toBeInTheDocument();
  });
});
