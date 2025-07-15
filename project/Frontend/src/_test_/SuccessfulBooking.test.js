import React from "react";
import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import StatusBooking from "../Components/StatusBooking";
import { UserContext } from "../Context/UserContext";
import axios from "axios";

// Mocks
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useParams: () => ({ reservaId: "123" }),
  useNavigate: () => jest.fn(),
}));

jest.mock("axios");

describe("SuccessfulBooking", () => {
  beforeEach(() => {
    localStorage.setItem("token", "mock-token");

    axios.get.mockResolvedValue({
      data: {
        id: 123,
        estado: "Confirmada",
      },
    });
  });

  afterEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
  });

  test("renders confirmation message for successful booking", async () => {
    render(
      <UserContext.Provider value={{ user: { id: 1 } }}>
        <StatusBooking />
      </UserContext.Provider>,
      { wrapper: BrowserRouter }
    );

    // Wait for elements to appear (async render after axios call)
    expect(await screen.findByText(/Su reserva se ha realizado con éxito/i)).toBeInTheDocument();
    expect(screen.getByText(/¡Muchas Gracias!/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /OK/i })).toBeInTheDocument();
  });
});
