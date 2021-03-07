using Retrospector.Core.Boundary;
using Retrospector.Core.Crud.Models;
using Retrospector.Core.Search.Models;
using Retrospector.Core.Tests.TestDoubles.Boundary;
using Retrospector.Core.Tests.Utilities;
using Xunit;

namespace Retrospector.Core.Tests.Tests.Boundary
{
    public class RequestRouterTests
    {
        private readonly IRequestRouter _router;
        private readonly UseCase_TestDouble<SearchRequest> _searchUse;
        private readonly UseCase_TestDouble<CrudRequest<Media>> _mediaUse;
        private readonly UseCase_TestDouble<CrudRequest<Review>> _reviewUse;
        private readonly UseCase_TestDouble<CrudRequest<Factoid>> _factoidUse;

        public RequestRouterTests()
        {
            _mediaUse = new UseCase_TestDouble<CrudRequest<Media>>();
            _reviewUse = new UseCase_TestDouble<CrudRequest<Review>>();
            _factoidUse = new UseCase_TestDouble<CrudRequest<Factoid>>();
            _searchUse = new UseCase_TestDouble<SearchRequest>();
            _router = new RequestRouter(_mediaUse, _reviewUse, _factoidUse, _searchUse);
        }

        [Fact]
        public void handles_null()
        {
            _router.Disseminate(null);

            Assert.Equal(Verify.Never, _reviewUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _factoidUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _mediaUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _searchUse.CountOfCallsTo_Execute);
        }

        [Fact]
        public void handles_request_of_wrong_type()
        {
            _router.Disseminate(new Request_TestDouble());

            Assert.Equal(Verify.Never, _reviewUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _factoidUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _mediaUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _searchUse.CountOfCallsTo_Execute);
        }

        [Fact]
        public void maps_crud_review_requests_to_review_use_case()
        {
            var request = new CrudRequest<Review>();

            _router.Disseminate(request);

            Assert.Equal(Verify.Once, _reviewUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _factoidUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _mediaUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _searchUse.CountOfCallsTo_Execute);
            Assert.Equal(request, _reviewUse.LastRequestPassedTo_Execute);
        }

        [Fact]
        public void maps_crud_factoid_requests_to_factoid_use_case()
        {
            var request = new CrudRequest<Factoid>();

            _router.Disseminate(request);

            Assert.Equal(Verify.Once, _factoidUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _reviewUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _mediaUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _searchUse.CountOfCallsTo_Execute);
            Assert.Equal(request, _factoidUse.LastRequestPassedTo_Execute);
        }

        [Fact]
        public void maps_crud_media_requests_to_media_use_case()
        {
            var request = new CrudRequest<Media>();

            _router.Disseminate(request);

            Assert.Equal(Verify.Once, _mediaUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _reviewUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _factoidUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _searchUse.CountOfCallsTo_Execute);
            Assert.Equal(request, _mediaUse.LastRequestPassedTo_Execute);
        }

        [Fact]
        public void maps_search_requests_to_search_use_case()
        {
            var request = new SearchRequest();

            _router.Disseminate(request);

            Assert.Equal(Verify.Once, _searchUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _mediaUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _reviewUse.CountOfCallsTo_Execute);
            Assert.Equal(Verify.Never, _factoidUse.CountOfCallsTo_Execute);
            Assert.Equal(request, _searchUse.LastRequestPassedTo_Execute);
        }
    }
}