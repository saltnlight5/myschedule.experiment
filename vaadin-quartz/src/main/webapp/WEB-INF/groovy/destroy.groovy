logger.debug("Cleaning up webapp.")

// Shutdown the scheduler.
scheduler = servletContext.getAttribute("scheduler")
scheduler.shutdown()

// Clean up any session attributes
def session = servletContext.getSession()
session.getAttributeNames().each{ name ->
	logger.debug("Removing session attribute: $name")
	session.removeAttribute(name)
}

