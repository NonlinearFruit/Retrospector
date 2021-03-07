using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Retrospector.DataStorage.Medias.Entities;

namespace Retrospector.DataStorage.Factoids.Entities
{
    [Table("Factoids")]
    public class FactoidEntity
    {
        [Key]
        public int Id { get; set; }
        public string Title { get; set; }
        public string Content { get; set; }
        public DateTime CreatedDate { get; set; }
        public int MediaId { get; set; }
        public virtual MediaEntity Media { get; set; }
    }
}